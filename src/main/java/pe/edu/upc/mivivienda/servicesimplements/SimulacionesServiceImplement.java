package pe.edu.upc.mivivienda.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.mivivienda.dtos.*;
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
    // Si ya no quieres guardar en BD el cronograma, puedes eliminar este repo
    // o al menos dejar de usarlo.
    // @Autowired
    // private ISimulacion_CronogramaRepository croRepo;

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
    public SimulacionConCronogramaResponse crearConCronograma(SimulacionRequest req) {
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
            String tipo = (req.bonoTipo() != null && !req.bonoTipo().isBlank())
                    ? req.bonoTipo().toUpperCase()
                    : resolveBonoTipoPorIngreso(prop); // "BBI" o "BBP"

            Optional<Bonos_reglas> opt = pickRule(tipo, req.moneda(), precioVenta);
            if (opt.isPresent()) {
                regla = opt.get();
                bonoMonto = regla.getMonto();
                bonoTipoEfectivo = regla.getNombre();
            }
        }

        // 4) Monto a financiar
        double P = round2(precioVenta - req.cuotaInicial() - bonoMonto);
        if (P < 0) P = 0;

        // 5) Cuota base francesa (sin seguros)
        double cuota = (P == 0 || i == 0) ? 0.0 :
                P * (i * Math.pow(1 + i, n)) / (Math.pow(1 + i, n) - 1);
        cuota = round2(cuota);

        // 6) Persistir cabecera Simulaciones (SOLO esto se guarda en BD)
        Simulaciones sim = new Simulaciones();
        sim.setPrecioVenta(precioVenta);
        sim.setCuotaInicial(req.cuotaInicial());
        sim.setMoneda(req.moneda());
        sim.setTiempoAnios(req.tiempoAnios());
        sim.setFrecuenciaPago(m);
        sim.setTipoAnio(req.tipoAnio());
        sim.setTipoGracia(req.tipoGracia());
        sim.setCantidadGracia(req.cantidadGracia());
        sim.setSeguroDesgravamen(ent.getSeguroDesgravamen());
        sim.setSeguroInmueble(ent.getSeguroInmueble());
        sim.setPropiedades_inmueble_id(prop);
        sim.setEntidades_financieras_entidadFinanciera_id(ent);
        sim.setTipoTasa("TEA");
        sim.setValorTasa(tea);
        sim.setCuotaFija(cuota);
        sim.setBonoAplica(req.aplicarBono());
        sim.setBono_Reglas_reglas_id(regla);
        sim.setBonoTipo(bonoTipoEfectivo);
        sim.setBonoMonto(bonoMonto);
        sim.setMontoPrestamo(P);

        sim = sR.save(sim);

        // 7) Costos adicionales (si llegan) -> estos sí se siguen guardando
        List<Costes_adicionales> costosGuardados = new java.util.ArrayList<>();
        if (req.costos() != null) {
            for (Costes_adicionalesDTO c : req.costos()) {
                Costes_adicionales ca = new Costes_adicionales();
                ca.setNombreCosto(c.getNombreCosto());
                ca.setValor(round2(c.getValor()));
                ca.setSimulaciones_simulacion_id(sim);
                costeRepo.save(ca);
                costosGuardados.add(ca);
            }
        }

        // 8) Construcción del cronograma SOLO EN MEMORIA (DTOs), NO BD
        double saldo = P;

        // tasas crudas de la entidad
        double segDesgMensualPct = ent.getSeguroDesgravamen(); // ej. 0.350 (% mensual)
        double segBienAnualPct   = ent.getSeguroInmueble();    // ej. 0.304 (% anual)
        double rawCobertura = ent.getValorCotizacionMax();     // p.ej. 90.0 si existe, 0.0 si no
        double coberturaMaxPct = (rawCobertura > 0.0 && rawCobertura <= 100.0)
                ? rawCobertura
                : 100.0;

        // prorrateos por periodo
        double mesesPorPeriodo = 12.0 / m; // 1 si mensual, 2 si bimestral, etc.
        double segDesgPeriodoFrac = (segDesgMensualPct * mesesPorPeriodo) / 100.0;   // fracción por periodo
        double segBienPeriodoFrac = (segBienAnualPct / m) / 100.0;                   // anual -> por periodo

        // base asegurada para el seguro del bien (sin tasación: usa valor de venta con tope)
        double baseAsegurada = round2(precioVenta * (coberturaMaxPct / 100.0));

        // >>> AQUÍ: desgravamen fijo por periodo, SIEMPRE igual, calculado sobre P <<<
        double segDesgFijo = round2(P * segDesgPeriodoFrac);

        List<Simulacion_CronogramaDTO> filas = new java.util.ArrayList<>();

// === Config: desgravamen nivelado (prima plana) ===
        final boolean NIVELAR_DESGRAVAMEN = true;
        long baseCents = 0L;   // ← visibles en el loop principal
        int  residuo   = 0;    // ← visibles en el loop principal

// ---- PRE-PASO: sumar el desgravamen "natural" (sin redondear) y nivelar en céntimos
        if (NIVELAR_DESGRAVAMEN) {
            double saldoTmp = P;
            double totalVar = 0.0;
            int g = nullSafe(req.cantidadGracia());

            for (int t = 1; t <= n; t++) {
                double interesTmp = saldoTmp * i;
                // ¡sin round aquí! sumamos con máxima precisión y recién al final pasamos a céntimos
                totalVar += (saldoTmp * segDesgPeriodoFrac);

                // avanzar saldoTmp con la misma lógica de gracia que el loop real
                if ("TOTAL".equalsIgnoreCase(req.tipoGracia()) && t <= g) {
                    saldoTmp = saldoTmp + interesTmp;       // capitaliza intereses
                } else if ("PARCIAL".equalsIgnoreCase(req.tipoGracia()) && t <= g) {
                    // saldoTmp se mantiene (solo intereses)
                } else {
                    double cuotaTmp = (t == n) ? (interesTmp + saldoTmp) : cuota; // última cierra saldo
                    double amortTmp = cuotaTmp - interesTmp;
                    saldoTmp = saldoTmp - amortTmp;
                }
            }

            long totalVarCents = Math.round(totalVar * 100.0); // a céntimos
            baseCents = totalVarCents / n;                     // parte entera por periodo
            residuo   = (int)(totalVarCents - baseCents * n);  // céntimos sobrantes a repartir (+1 en las primeras 'residuo')
        }

// ---- LOOP PRINCIPAL
        for (int t = 1; t <= n; t++) {
            double saldoIni = round2(saldo);
            double interes  = round2(saldoIni * i);

            // Desgravamen:
            double segDesg;
            if (NIVELAR_DESGRAVAMEN) {
                long cents = baseCents + (t <= residuo ? 1 : 0); // reparte los sobrantes primero
                segDesg = cents / 100.0;
            } else {
                segDesg = round2(saldoIni * segDesgPeriodoFrac); // modo variable
            }

            // Seguro de bien prorrateado por periodo sobre base topeada:
            double segInm = round2(baseAsegurada * segBienPeriodoFrac);

            double amort, cuotaPeriodo, saldoFin;
            if ("TOTAL".equalsIgnoreCase(req.tipoGracia()) && t <= nullSafe(req.cantidadGracia())) {
                amort = 0.0;
                cuotaPeriodo = 0.0;
                saldoFin = round2(saldoIni + interes);
            } else if ("PARCIAL".equalsIgnoreCase(req.tipoGracia()) && t <= nullSafe(req.cantidadGracia())) {
                amort = 0.0;
                cuotaPeriodo = interes;
                saldoFin = saldoIni;
            } else {
                cuotaPeriodo = cuota;
                amort = round2(cuotaPeriodo - interes);
                if (t == n) { amort = saldoIni; cuotaPeriodo = round2(interes + amort); }
                saldoFin = round2(saldoIni - amort);
            }

            double cuotaTotal = round2(cuotaPeriodo + segDesg + segInm);

            Simulacion_CronogramaDTO dto = new Simulacion_CronogramaDTO();
            dto.setPeriodo(t);
            dto.setSaldoInicial(saldoIni);
            dto.setSaldoInicialIndexado(saldoIni);
            dto.setInteres(interes);
            dto.setCuota(cuotaPeriodo);
            dto.setAmortizacion(amort);
            dto.setSeguroDesgravamen(segDesg);
            dto.setSeguroInmueble(segInm);
            dto.setSaldoFinal(saldoFin);
            dto.setCuotaTotal(cuotaTotal);
            dto.setSimulaciones_simulacion_id(sim);
            filas.add(dto);

            saldo = saldoFin;
        }

        // 9) Armar respuesta: simulación + cronograma, usando tu SimulacionesDTO
        SimulacionesDTO simDto = new SimulacionesDTO();
        simDto.setSimulacion_id(sim.getSimulacion_id());
        simDto.setPrecioVenta(sim.getPrecioVenta());
        simDto.setCuotaInicial(sim.getCuotaInicial());
        simDto.setMontoPrestamo(sim.getMontoPrestamo());
        simDto.setMoneda(sim.getMoneda());
        simDto.setTiempoAnios(sim.getTiempoAnios());
        simDto.setFrecuenciaPago(sim.getFrecuenciaPago());
        simDto.setTipoAnio(sim.getTipoAnio());
        simDto.setTipoGracia(sim.getTipoGracia());
        simDto.setCantidadGracia(sim.getCantidadGracia());
        simDto.setSeguroDesgravamen(sim.getSeguroDesgravamen());
        simDto.setSeguroInmueble(sim.getSeguroInmueble());
        simDto.setPropiedades_inmueble_id(sim.getPropiedades_inmueble_id());
        simDto.setEntidades_financieras_entidadFinanciera_id(sim.getEntidades_financieras_entidadFinanciera_id());
        simDto.setTipoTasa(sim.getTipoTasa());
        simDto.setValorTasa(sim.getValorTasa());
        simDto.setCuotaFija(sim.getCuotaFija());
        simDto.setBonoAplica(sim.getBonoAplica());
        simDto.setBonoTipo(sim.getBonoTipo());
        simDto.setBonoMonto(sim.getBonoMonto());
        simDto.setBono_Reglas_reglas_id(sim.getBono_Reglas_reglas_id());
        simDto.setCostos(costosGuardados);

        SimulacionConCronogramaResponse resp = new SimulacionConCronogramaResponse();
        resp.setSimulacion(simDto);
        resp.setCronograma(filas);

        return resp;
    }

    private static int nullSafe(Integer v) { return v == null ? 0 : v; }
    private static double round2(double v) { return Math.round(v * 100.0) / 100.0; }
    private double resolveTeaPct(Entidades_financieras ent, Double teaInputPctOrFrac) {
        double minPct = ent.getTEAmin(); // ej. 8.10
        double maxPct = ent.getTEAmax(); // ej. 12.50

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

    // SimulacionesServiceImplement.java
    public SimulacionConCronogramaResponse recalcularHoja(int id){
        var sim = sR.findById(id).orElseThrow(() -> new IllegalArgumentException("Simulación no existe"));
        // Reconstruye un request con lo guardado y reutiliza tu método:
        var req = new SimulacionRequest(
                sim.getPropiedades_inmueble_id().getInmueble_id(),
                sim.getEntidades_financieras_entidadFinanciera_id().getEntidadFinanciera_id(),
                sim.getMoneda(), sim.getPrecioVenta(), sim.getCuotaInicial(),
                sim.getTiempoAnios(), sim.getFrecuenciaPago(), sim.getTipoAnio(),
                sim.getTipoGracia(), sim.getCantidadGracia(),
                sim.getBonoAplica(), sim.getBonoTipo(),   // bonoTipo opcional
                sim.getValorTasa() * 100.0,               // a % si tu request espera %
                null                                      // costos: en la hoja no hace falta
        );
        return crearConCronograma(req); // reutiliza la lógica ya probada
    }

}

