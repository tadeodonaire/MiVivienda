package pe.edu.upc.mivivienda.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.mivivienda.dtos.Costes_adicionalesDTO;
import pe.edu.upc.mivivienda.dtos.SimulacionRequest;
import pe.edu.upc.mivivienda.dtos.Simulacion_CronogramaDTO;
import pe.edu.upc.mivivienda.entities.*;
import pe.edu.upc.mivivienda.repositories.*;
import pe.edu.upc.mivivienda.servicesinterfaces.ISimulacionesService;

import java.util.List;
import java.util.Optional;

@Service
public class SimulacionesServiceImplement implements ISimulacionesService {
    @Autowired
    private ISimulacionesRepository sR;
    @Autowired
    private IPropiedadesRepository propRepo;
    @Autowired
    private IEntidades_financierasRepository entRepo;
    @Autowired
    private IBonos_ReglasRepository bonoRepo;
    @Autowired
    private ICostes_adicionalesRepository costeRepo;
    @Autowired
    private ISimulacion_CronogramaRepository croRepo;

    @Override
    public void insert(Simulaciones simulaciones) {
        sR.save(simulaciones);
    }

    @Override
    public List<Simulaciones> list() {
        return sR.findAll();
    }

    @Override
    public void update(Simulaciones simulaciones) {
        sR.save(simulaciones);
    }

    @Override
    public Simulaciones listarId(int id) {
        return sR.findById(id).orElse(new  Simulaciones());
    }

    @Override
    public void delete(int id) {
        sR.deleteById(id);
    }


    @Override
    public Simulaciones crearConCronograma(SimulacionRequest req) {
        // 1) Traer FKs y validar
        Propiedades prop = propRepo.findById(req.propiedadId())
                .orElseThrow(() -> new IllegalArgumentException("Propiedad no existe"));
        Entidades_financieras ent = entRepo.findById(req.entidadFinancieraId())
                .orElseThrow(() -> new IllegalArgumentException("Entidad financiera no existe"));

        double precioVenta = (req.precioVenta() > 0 ? req.precioVenta() : prop.getPrecioInmueble());
        if (!"PEN".equalsIgnoreCase(req.moneda())) {
            throw new IllegalArgumentException("Por ahora solo PEN");
        }

        // 2) Determinar TEA
        double teaPct = resolveTeaPct(ent, req.tasaEfectivaAnual()); // [%] validada y dentro de rango
        double tea = teaPct / 100.0;            // fracción anual
        int m = req.frecuenciaPago();           // 12, 6, etc.
        int n = req.tiempoAnios() * m;
        double i = Math.pow(1.0 + tea, 1.0 / m) - 1.0; // tasa efectiva por periodo

        // 3) Bono (opcional)
        Bonos_reglas regla = null;
        double bonoMonto = 0.0;
        String bonoTipoEfectivo = "SIN";

        if (Boolean.TRUE.equals(req.aplicarBono())) {
            // decide tipo si no viene: BBI si ingreso <= 4746; si no, BBP
            String tipo = (req.bonoTipo() != null && !req.bonoTipo().isBlank())
                    ? req.bonoTipo().toUpperCase()
                    : resolveBonoTipoPorIngreso(prop); // "BBI" o "BBP"

            Optional<Bonos_reglas> opt = pickRule(tipo, req.moneda(), precioVenta);
            if (opt.isPresent()) {
                regla = opt.get();
                bonoMonto = regla.getMonto();
                bonoTipoEfectivo = regla.getNombre(); // guarda exactamente lo que aplicaste
            } // si no hay banda, se queda SIN bono
        }

        // 4) Monto a financiar
        double P = round2(precioVenta - req.cuotaInicial() - bonoMonto);
        if (P < 0) P = 0;

        // 5) Cuota base francesa (sin seguros)
        double cuota = (P == 0 || i == 0) ? 0.0 :
                P * (i * Math.pow(1 + i, n)) / (Math.pow(1 + i, n) - 1);
        cuota = round2(cuota);

        // 6) Persistir cabecera Simulaciones
        Simulaciones sim = new Simulaciones();
        sim.setPrecioVenta(precioVenta);
        sim.setCuotaInicial(req.cuotaInicial());
        sim.setMoneda(req.moneda());
        sim.setTiempoAnios(req.tiempoAnios());
        sim.setFrecuenciaPago(m);
        sim.setTipoAnio(req.tipoAnio());
        sim.setTipoGracia(req.tipoGracia());
        sim.setCantidadGracia(req.cantidadGracia());
        sim.setSeguroDesgravamen(ent.getSeguroDesgravamen()); // % mensual como tienes en BD
        sim.setSeguroInmueble(ent.getSeguroInmueble());       // % mensual
        sim.setPropiedades_inmueble_id(prop);
        sim.setEntidades_financieras_entidadFinanciera_id(ent);
        sim.setTipoTasa("TEA");
        sim.setValorTasa(tea);            // fracción anual
        sim.setCuotaFija(cuota);
        sim.setBonoAplica(req.aplicarBono());
        sim.setBono_Reglas_reglas_id(regla);
        sim.setBonoTipo(bonoTipoEfectivo); // "BBP"/"BBI"/"SIN"
        sim.setBonoMonto(bonoMonto);
        sim.setMontoPrestamo(P);

        sim = sR.save(sim);

        // 7) Costos adicionales (si llegan)
        if (req.costos() != null) {
            for (Costes_adicionalesDTO c : req.costos()) {
                Costes_adicionales ca = new Costes_adicionales();
                ca.setNombreCosto(c.getNombreCosto());
                ca.setValor(round2(c.getValor()));
                ca.setSimulaciones_simulacion_id(sim);
                costeRepo.save(ca);
            }
        }

        // 8) Construcción del cronograma
        double saldo = P;
        double tasaDesg = ent.getSeguroDesgravamen()/100.0; // convertir en fracción mensual (0.075/100 = 0.00075 por ej.)
        double tasaInm  = ent.getSeguroInmueble()/100.00;    // fracción mensual
        for (int t = 1; t <= n; t++) {
            double saldoIni = round2(saldo);
            double interes = round2(saldoIni * i);
            double segDesg = round2(saldoIni * tasaDesg);
            double segInm  = round2(precioVenta * tasaInm);

            double amort, cuotaPeriodo, saldoFin;

            if ("TOTAL".equalsIgnoreCase(req.tipoGracia()) && t <= nullSafe(req.cantidadGracia())) {
                // Capitaliza intereses; no hay cuota ni amortización
                amort = 0.0;
                cuotaPeriodo = 0.0;
                saldoFin = round2(saldoIni + interes);
            } else if ("PARCIAL".equalsIgnoreCase(req.tipoGracia()) && t <= nullSafe(req.cantidadGracia())) {
                // Solo intereses; saldo no baja
                amort = 0.0;
                cuotaPeriodo = interes; // la columna "cuota" guarda interés+amortización
                saldoFin = saldoIni;
            } else {
                // Normal francés
                cuotaPeriodo = cuota;
                amort = round2(cuotaPeriodo - interes);
                // Ajuste de último periodo para dejar saldo 0.00
                if (t == n) {
                    amort = saldoIni;           // liquida todo el principal
                    cuotaPeriodo = round2(interes + amort);
                }
                saldoFin = round2(saldoIni - amort);
            }

            Simulacion_Cronograma fila = new Simulacion_Cronograma();
            fila.setSimulaciones_simulacion_id(sim);
            fila.setPeriodo(t);
            fila.setSaldoInicial(saldoIni);
            fila.setSaldoInicialIndexado(saldoIni); // si no indexas
            fila.setInteres(interes);
            fila.setCuota(cuotaPeriodo);
            fila.setAmortizacion(amort);
            fila.setSeguroDesgravamen(segDesg);
            fila.setSeguroInmueble(segInm);
            fila.setSaldoFinal(saldoFin);
            croRepo.save(fila);

            saldo = saldoFin;
        }

        return sim;
    }

    private static int nullSafe(Integer v) { return v == null ? 0 : v; }
    private static double round2(double v) { return Math.round(v * 100.0) / 100.0; }
    private double resolveTeaPct(Entidades_financieras ent, Double teaInputPctOrFrac) {
        double minPct = ent.getTEAmin();  // ej. 8.10
        double maxPct = ent.getTEAmax();  // ej. 12.50

        // Si no viene, usa la mínima de la entidad
        if (teaInputPctOrFrac == null) return minPct;

        // Acepta fracción (0.1095) o porcentaje (10.95)
        double pct = teaInputPctOrFrac;
        if (pct > 0 && pct < 1) pct *= 100.0;

        // Elige UNA de estas políticas:

        // (A) Validar y lanzar error si está fuera:
        if (pct < minPct || pct > maxPct) {
            throw new IllegalArgumentException(
                    String.format("TEA %.4f%% fuera de rango permitido por %s [%.4f%% – %.4f%%]",
                            pct, ent.getNombre(), minPct, maxPct));
        }

        // (B) (Alternativa) Forzar al rango (clamp):
        // pct = Math.max(minPct, Math.min(maxPct, pct));

        return pct; // aún en %
    }
    private Optional<Bonos_reglas> pickRule(String tipo, String moneda, double precio) {
        var list = bonoRepo.findBands(tipo, moneda, precio);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    // Si no se envía bonoTipo, decide por ingreso del cliente de la propiedad
    private String resolveBonoTipoPorIngreso(Propiedades prop) {
        double ingreso = 0.0;
        if (prop.getClientes_cliente_id() != null) {
            ingreso = prop.getClientes_cliente_id().getIngresosMensuales();
        }
        return (ingreso > 0 && ingreso <= 4746.0) ? "BBI" : "BBP";
    }
}

