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
    private static final java.util.Set<String> TIPOS
            = java.util.Set.of("INICIAL","RECURRENTE");

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
// Para prorrateos de costos recurrentes
        double mesesPorPeriodo = 12.0 / m;

// === Costos adicionales del REQUEST (aún no persisto, pero los uso para el cálculo) ===
        double sumaInicial = 0.0;      // aumenta P
        double costoRecPeriodo = 0.0;  // suma al flujo de cada periodo

        if (req.costos() != null) {
            for (Costes_adicionalesDTO c : req.costos()) {
                String tipo = (c.getTipo() == null ? "INICIAL" : c.getTipo().toUpperCase());
                double v = round2(c.getValor());
                if ("INICIAL".equals(tipo)) sumaInicial += v;
                else /* RECURRENTE */      costoRecPeriodo += v; // por periodo, sin prorrateos
            }
        }

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

        // 4) Monto a financiar (saldo a financiar – bono + costos INICIALES)
        double P = round2(precioVenta - req.cuotaInicial() - bonoMonto);
        if (P < 0) P = 0;
        P = round2(P + sumaInicial); // 👈 agrega costos INICIALES a P

// 5) Cuota base francesa (sin seguros) usando P ya ajustado
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

        // 7) Persistir costos (con tipo/periodicidad)
        List<Costes_adicionales> costosGuardados = new java.util.ArrayList<>();
        if (req.costos() != null) {
            for (Costes_adicionalesDTO c : req.costos()) {
                String tipo = (c.getTipo() == null ? "INICIAL" : c.getTipo().toUpperCase());

                Costes_adicionales ca = new Costes_adicionales();
                ca.setNombreCosto(c.getNombreCosto());
                ca.setValor(round2(c.getValor()));
                ca.setTipo(tipo);               // << nuevo (String)
                ca.setSimulaciones_simulacion_id(sim);
                costeRepo.save(ca);
                costosGuardados.add(ca);
            }
        }

        // === 8) Cronograma en memoria ===
        double saldo = P;

// Tasas/porcentajes
        double segDesgMensualPct = ent.getSeguroDesgravamen();   // p.ej. 0.045 (% mensual)
        double segBienAnualPct   = ent.getSeguroInmueble();      // p.ej. 0.400 (% anual)
        double coberturaMaxPct = 100.0;

// Prorrateos por periodo
        double segDesPerPeriodo  = (segDesgMensualPct * mesesPorPeriodo) / 100.0; // usa el mismo
        double segBienPerPeriodo = (segBienAnualPct  / m)              / 100.0;
        double baseAsegurada      = round2(precioVenta * (coberturaMaxPct / 100.0));
        double comisionPer  = 0.0; // si luego las persistes, léelas de la entidad/req
        double portesPer    = 0.0;
        double gastosAdmPer = 0.0;
        List<Simulacion_CronogramaDTO> filas = new java.util.ArrayList<>();

// ----- Gracia -----
        int g  = nullSafe(req.cantidadGracia());
        String tg = (req.tipoGracia() == null ? "SIN_GRACIA" : req.tipoGracia().toUpperCase());
        if (!"TOTAL".equals(tg) && !"PARCIAL".equals(tg)) g = 0;     // solo cuenta si es TOTAL o PARCIAL
        g = Math.max(0, Math.min(g, n));                             // clamp

// 8.1) Periodos en gracia (no cambian n, solo afectan los g primeros)
        for (int t = 1; t <= g; t++) {
            double saldoIni = round2(saldo);
            double interes  = round2(saldoIni * i);
            double segDes   = round2(saldoIni * segDesPerPeriodo);        // variable
            double segInm   = round2(baseAsegurada * segBienPerPeriodo);

            double amort, cuotaIncSeg, saldoFin;
            if ("TOTAL".equals(tg)) {
                cuotaIncSeg = 0.0;            // no se paga cuota base
                amort       = 0.0;
                saldoFin    = round2(saldoIni + interes);  // capitaliza interés
            } else { // PARCIAL
                cuotaIncSeg = interes;         // paga solo interés
                amort       = 0.0;
                saldoFin    = saldoIni;        // saldo no cambia
            }

            double cuotaTotal = round2(cuotaIncSeg + segDes + segInm);
            double flujo      = round2(cuotaIncSeg + segDes + segInm + costoRecPeriodo);

            Simulacion_CronogramaDTO dto = new Simulacion_CronogramaDTO();
            dto.setPeriodo(t);
            dto.setSaldoInicial(saldoIni);
            dto.setSaldoInicialIndexado(saldoIni);
            dto.setInteres(interes);
            dto.setCuota(cuotaIncSeg);        // *** "Cuota (inc SegDes)" en gracia es 0 o interés
            dto.setAmortizacion(amort);
            dto.setSeguroDesgravamen(segDes);
            dto.setSeguroInmueble(segInm);
            dto.setSaldoFinal(saldoFin);
            dto.setCuotaTotal(cuotaTotal);
            dto.setFlujo(flujo);                 // ← NUEVO
            dto.setSimulaciones_simulacion_id(sim);
            filas.add(dto);

            saldo = saldoFin;
        }

// --- 8.2) Recalcular CUOTA CONSTANTE (Interbank) para los periodos restantes ---
        int nRest = n - g;
        double r = i + segDesPerPeriodo;           // TEP + % desgravamen por periodo (fracción)
        double cuotaIncSegConst = 0.0;

        if (nRest > 0) {
            // saldo al terminar la gracia (mayor si fue TOTAL, igual si fue PARCIAL)
            double saldoPostGracia = saldo; // 'saldo' viene de la etapa de gracia
            cuotaIncSegConst = (saldoPostGracia == 0 || r == 0) ? 0.0 :
                    round2( saldoPostGracia * (r * Math.pow(1 + r, nRest)) / (Math.pow(1 + r, nRest) - 1) );
        }

// --- 8.3) Periodos normales con cuota CONSTANTE (inc. seg. desgravamen) ---
        for (int k = 1; k <= nRest; k++) {
            int t = g + k;

            double saldoIni = round2(saldo);
            double interes  = round2(saldoIni * i);
            double segDes   = round2(saldoIni * segDesPerPeriodo);     // variable (sobre saldo)
            double segInm   = round2(baseAsegurada * segBienPerPeriodo);

            double cuotaPeriodoIncSeg = cuotaIncSegConst;
            double amort = round2(cuotaPeriodoIncSeg - interes - segDes);

            // Último periodo: cerrar exacto
            if (k == nRest) {
                amort = saldoIni;
                cuotaPeriodoIncSeg = round2(interes + segDes + amort);
            }

            double saldoFin   = round2(saldoIni - amort);
            double cuotaTotal = round2(cuotaPeriodoIncSeg + segInm);
            double flujo      = round2(cuotaPeriodoIncSeg + segInm + costoRecPeriodo);

            Simulacion_CronogramaDTO dto = new Simulacion_CronogramaDTO();
            dto.setPeriodo(t);
            dto.setSaldoInicial(saldoIni);
            dto.setSaldoInicialIndexado(saldoIni);
            dto.setInteres(interes);
            dto.setCuota(cuotaPeriodoIncSeg);      // "Cuota (inc SegDes)" CONSTANTE
            dto.setAmortizacion(amort);
            dto.setSeguroDesgravamen(segDes);
            dto.setSeguroInmueble(segInm);
            dto.setSaldoFinal(saldoFin);
            dto.setCuotaTotal(cuotaTotal);
            dto.setFlujo(flujo);                 // ← NUEVO
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
        var sim = sR.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Simulación no existe"));

        var costos = costeRepo.findAllBySimulacionId(id); // ← JPQL de arriba

        var costosDTO = costos.stream().map(c -> {
            var d = new Costes_adicionalesDTO();
            d.setNombreCosto(c.getNombreCosto());
            d.setValor(c.getValor());
            d.setTipo(c.getTipo());           // "INICIAL" | "RECURRENTE"
            return d;
        }).toList();

        var req = new SimulacionRequest(
                sim.getPropiedades_inmueble_id().getInmueble_id(),
                sim.getEntidades_financieras_entidadFinanciera_id().getEntidadFinanciera_id(),
                sim.getMoneda(), sim.getPrecioVenta(), sim.getCuotaInicial(),
                sim.getTiempoAnios(), sim.getFrecuenciaPago(), sim.getTipoAnio(),
                sim.getTipoGracia(), sim.getCantidadGracia(),
                sim.getBonoAplica(), sim.getBonoTipo(),
                sim.getValorTasa() * 100.0,
                costosDTO                               // ← incluye costos guardados
        );

        return crearConCronograma(req);
    }


}

