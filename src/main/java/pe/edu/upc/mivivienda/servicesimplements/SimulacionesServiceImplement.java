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

    // Máximo global de meses/periodos de gracia permitido por los bancos
    private static final int MAX_MESES_GRACIA = 5;

    @Autowired
    private TipoCambioService tipoCambioService;
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

        // 🔹 Moneda que eligió el usuario para VER la simulación
        String monedaVista = (req.moneda() == null ? "PEN" : req.moneda().toUpperCase());
        boolean mostrarEnUsd = "USD".equals(monedaVista);

        // 🔹 Tipo de cambio PEN -> USD (por defecto 1 si se ve en PEN)
        double tc = 1.0;
        if (mostrarEnUsd) {
            tc = tipoCambioService.obtenerTipoCambio("PEN", "USD");
        }

        // 1) Traer FKs y validar
        Propiedades prop = propRepo.findById(req.propiedadId())
                .orElseThrow(() -> new IllegalArgumentException("Propiedad no existe"));
        Entidades_financieras ent = entRepo.findById(req.entidadFinancieraId())
                .orElseThrow(() -> new IllegalArgumentException("Entidad financiera no existe"));

        // 🔹 Siempre calculamos EN SOLES
        double precioVenta = (req.precioVenta() > 0 ? req.precioVenta() : prop.getPrecioInmueble());
        String monedaBase = "PEN";

        // 2) Determinar TEA
        double teaPct = resolveTeaPct(ent, req.tasaEfectivaAnual()); // [%]
        double tea = teaPct / 100.0;            // fracción anual
        int m = req.frecuenciaPago();           // 12, 6, etc.
        int n = req.tiempoAnios() * m;
        double i = Math.pow(1.0 + tea, 1.0 / m) - 1.0;

        double mesesPorPeriodo = 12.0 / m;

        // ----- Gracia -----
        int gMesesSolicitada = nullSafe(req.cantidadGracia());
        String tg = (req.tipoGracia() == null ? "SIN_GRACIA" : req.tipoGracia().toUpperCase());

        if (!"TOTAL".equals(tg) && !"PARCIAL".equals(tg)) {
            gMesesSolicitada = 0;
        }

        int gMeses = Math.max(0, Math.min(gMesesSolicitada, MAX_MESES_GRACIA));

        int gPeriodos = 0;
        if (gMeses > 0 && mesesPorPeriodo > 0) {
            gPeriodos = (int) Math.floor(gMeses / mesesPorPeriodo);
        }
        gPeriodos = Math.max(0, Math.min(gPeriodos, n));

        double cokPct = (req.tasaDescuentoAnual() == null ? 25.0 : req.tasaDescuentoAnual());
        double cok = cokPct / 100.0;

        double sumaInicial = 0.0;
        double costoRecPeriodo = 0.0;

        if (req.costos() != null) {
            for (Costes_adicionalesDTO c : req.costos()) {
                String tipo = (c.getTipo() == null ? "INICIAL" : c.getTipo().toUpperCase());
                double v = round2(c.getValor());
                if ("INICIAL".equals(tipo)) sumaInicial += v;
                else costoRecPeriodo += v;
            }
        }

        // 3) Bono (opcional)
        Bonos_reglas regla = null;
        double bonoMonto = 0.0;
        String bonoTipoEfectivo = "SIN";

        if (Boolean.TRUE.equals(req.aplicarBono())) {
            if (req.bonoReglaId() != null) {
                // bono explícito
                regla = bonoRepo.findById(req.bonoReglaId())
                        .orElseThrow(() -> new IllegalArgumentException("Bono no existe"));
                bonoMonto = regla.getMonto();
                bonoTipoEfectivo = regla.getNombre();
            } else {
                // lógica de BBI/BBP
                String tipo = (req.bonoTipo() != null && !req.bonoTipo().isBlank())
                        ? req.bonoTipo().toUpperCase()
                        : resolveBonoTipoPorIngreso(prop);

                // 🔹 usamos monedaBase ("PEN"), no "moneda" antigua
                Optional<Bonos_reglas> opt = pickRule(tipo, monedaBase, precioVenta);
                if (opt.isPresent()) {
                    regla = opt.get();
                    bonoMonto = regla.getMonto();
                    bonoTipoEfectivo = regla.getNombre();
                }
            }
        }

        // 4) Monto a financiar
        double P = round2(precioVenta - req.cuotaInicial() - bonoMonto);
        if (P < 0) P = 0;
        P = round2(P + sumaInicial);

        // 5) Cuota francesa base
        double cuota = (P == 0 || i == 0) ? 0.0 :
                P * (i * Math.pow(1 + i, n)) / (Math.pow(1 + i, n) - 1);
        cuota = round2(cuota);

        // 6) Persistir cabecera SIEMPRE en PEN
        Simulaciones sim = new Simulaciones();
        sim.setPrecioVenta(precioVenta);
        sim.setCuotaInicial(req.cuotaInicial());
        sim.setMoneda(monedaBase); // "PEN"
        sim.setTiempoAnios(req.tiempoAnios());
        sim.setFrecuenciaPago(m);
        sim.setTipoAnio(req.tipoAnio());
        sim.setTipoGracia(req.tipoGracia());
        sim.setCantidadGracia(gMeses);
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
        sim.setTasaDescuentoAnual(cokPct);

        sim = sR.save(sim);

        // 7) Guardar costos
        List<Costes_adicionales> costosGuardados = new java.util.ArrayList<>();
        if (req.costos() != null) {
            for (Costes_adicionalesDTO c : req.costos()) {
                String tipo = (c.getTipo() == null ? "INICIAL" : c.getTipo().toUpperCase());

                Costes_adicionales ca = new Costes_adicionales();
                ca.setNombreCosto(c.getNombreCosto());
                ca.setValor(round2(c.getValor()));
                ca.setTipo(tipo);
                ca.setSimulaciones_simulacion_id(sim);
                costeRepo.save(ca);
                costosGuardados.add(ca);
            }
        }

        // 8) Cronograma en memoria (todo en PEN)
        double saldo = P;

        double segDesgMensualPct = ent.getSeguroDesgravamen();
        double segBienAnualPct   = ent.getSeguroInmueble();
        double coberturaMaxPct   = 100.0;

        double segDesPerPeriodo  = (segDesgMensualPct * mesesPorPeriodo) / 100.0;
        double segBienPerPeriodo = (segBienAnualPct  / m) / 100.0;
        double baseAsegurada     = round2(precioVenta * (coberturaMaxPct / 100.0));

        List<Simulacion_CronogramaDTO> filas = new java.util.ArrayList<>();

        // 8.1) Periodos en gracia
        for (int t = 1; t <= gPeriodos; t++) {
            double saldoIni = round2(saldo);
            double interes  = round2(saldoIni * i);
            double segDes   = round2(saldoIni * segDesPerPeriodo);
            double segInm   = round2(baseAsegurada * segBienPerPeriodo);

            double amort, cuotaIncSeg, saldoFin;
            if ("TOTAL".equals(tg)) {
                cuotaIncSeg = 0.0;
                amort       = 0.0;
                saldoFin    = round2(saldoIni + interes);
            } else { // PARCIAL
                cuotaIncSeg = interes;
                amort       = 0.0;
                saldoFin    = saldoIni;
            }

            double cuotaTotal = round2(cuotaIncSeg + segDes + segInm);
            double flujo      = round2(-(cuotaTotal + costoRecPeriodo));

            Simulacion_CronogramaDTO dto = new Simulacion_CronogramaDTO();
            dto.setPeriodo(t);
            dto.setSaldoInicial(saldoIni);
            dto.setSaldoInicialIndexado(saldoIni);
            dto.setInteres(interes);
            dto.setCuota(cuotaIncSeg);
            dto.setAmortizacion(amort);
            dto.setSeguroDesgravamen(segDes);
            dto.setSeguroInmueble(segInm);
            dto.setSaldoFinal(saldoFin);
            dto.setCuotaTotal(cuotaTotal);
            dto.setFlujo(flujo);
            dto.setSimulaciones_simulacion_id(sim);
            filas.add(dto);

            saldo = saldoFin;
        }

        // 8.2) Recalcular cuota constante para el resto
        int nRest = n - gPeriodos;
        double r = i + segDesPerPeriodo;
        double cuotaIncSegConst = 0.0;

        if (nRest > 0) {
            double saldoPostGracia = saldo;
            cuotaIncSegConst = (saldoPostGracia == 0 || r == 0) ? 0.0 :
                    round2(saldoPostGracia * (r * Math.pow(1 + r, nRest)) / (Math.pow(1 + r, nRest) - 1));
        }

        // 8.3) Periodos normales
        for (int k = 1; k <= nRest; k++) {
            int t = gPeriodos + k;

            double saldoIni = round2(saldo);
            double interes  = round2(saldoIni * i);
            double segDes   = round2(saldoIni * segDesPerPeriodo);
            double segInm   = round2(baseAsegurada * segBienPerPeriodo);

            double cuotaPeriodoIncSeg = cuotaIncSegConst;
            double amort = round2(cuotaPeriodoIncSeg - interes - segDes);

            if (k == nRest) {
                amort = saldoIni;
                cuotaPeriodoIncSeg = round2(interes + segDes + amort);
            }

            double saldoFin   = round2(saldoIni - amort);
            double cuotaTotal = round2(cuotaPeriodoIncSeg + segInm);
            double flujo      = round2(-(cuotaTotal + costoRecPeriodo));

            Simulacion_CronogramaDTO dto = new Simulacion_CronogramaDTO();
            dto.setPeriodo(t);
            dto.setSaldoInicial(saldoIni);
            dto.setSaldoInicialIndexado(saldoIni);
            dto.setInteres(interes);
            dto.setCuota(cuotaPeriodoIncSeg);
            dto.setAmortizacion(amort);
            dto.setSeguroDesgravamen(segDes);
            dto.setSeguroInmueble(segInm);
            dto.setSaldoFinal(saldoFin);
            dto.setCuotaTotal(cuotaTotal);
            dto.setFlujo(flujo);
            dto.setSimulaciones_simulacion_id(sim);
            filas.add(dto);

            saldo = saldoFin;
        }

        // 9) VAN, TIR, TCEA (se calculan con flujos en PEN)
        int diasAnio = req.tipoAnio();
        double diasPorPeriodo = diasAnio / (double) m;
        double kPer = Math.pow(1.0 + cok, diasPorPeriodo / diasAnio) - 1.0;

        java.util.List<Double> flujos = new java.util.ArrayList<>();
        double flujo0 = P;
        flujos.add(flujo0);
        for (Simulacion_CronogramaDTO f : filas) {
            flujos.add(f.getFlujo());
        }

        double van = calcularVAN(flujos, kPer);
        double tirPeriodo = calcularTIRPeriodo(flujos);
        double tirPeriodoPct = round2(tirPeriodo * 100.0);
        double tcea = Math.pow(1.0 + tirPeriodo, m) - 1.0;
        double tceaPct = round2(tcea * 100.0);

        // 10) DTO de salida (primero en PEN)
        SimulacionesDTO simDto = new SimulacionesDTO();
        simDto.setSimulacion_id(sim.getSimulacion_id());
        simDto.setPrecioVenta(sim.getPrecioVenta());
        simDto.setCuotaInicial(sim.getCuotaInicial());
        simDto.setMontoPrestamo(sim.getMontoPrestamo());
        simDto.setMoneda(sim.getMoneda()); // "PEN"
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
        simDto.setTasaDescuentoAnual(sim.getTasaDescuentoAnual());

        SimulacionConCronogramaResponse resp = new SimulacionConCronogramaResponse();
        resp.setSimulacion(simDto);
        resp.setCronograma(filas);
        resp.setVan(van);
        resp.setTirPeriodo(tirPeriodoPct);
        resp.setTcea(tceaPct);

        // 🔹 Conversión a USD SOLO para la respuesta, si el usuario pidió USD
        if (mostrarEnUsd && tc > 0) {
            simDto.setMoneda("USD");

            simDto.setPrecioVenta( round2(simDto.getPrecioVenta() * tc) );
            simDto.setCuotaInicial( round2(simDto.getCuotaInicial() * tc) );
            simDto.setMontoPrestamo( round2(simDto.getMontoPrestamo() * tc) );
            simDto.setCuotaFija( round2(simDto.getCuotaFija() * tc) );
            simDto.setBonoMonto( round2(simDto.getBonoMonto() * tc) );

            resp.setVan( round2(resp.getVan() * tc) );

            for (Simulacion_CronogramaDTO fila : filas) {
                fila.setSaldoInicial( round2(fila.getSaldoInicial() * tc) );
                fila.setSaldoInicialIndexado( round2(fila.getSaldoInicialIndexado() * tc) );
                fila.setInteres( round2(fila.getInteres() * tc) );
                fila.setCuota( round2(fila.getCuota() * tc) );
                fila.setAmortizacion( round2(fila.getAmortizacion() * tc) );
                fila.setSeguroDesgravamen( round2(fila.getSeguroDesgravamen() * tc) );
                fila.setSeguroInmueble( round2(fila.getSeguroInmueble() * tc) );
                fila.setCuotaTotal( round2(fila.getCuotaTotal() * tc) );
                fila.setSaldoFinal( round2(fila.getSaldoFinal() * tc) );
                fila.setFlujo( round2(fila.getFlujo() * tc) );
            }
        }

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

        return pct; // aún en %
    }

    // VAN con tasa por periodo (kPer)
    private static double calcularVAN(java.util.List<Double> flujos, double kPer) {
        double npv = 0.0;
        for (int t = 0; t < flujos.size(); t++) {
            double ft = flujos.get(t);
            npv += ft / Math.pow(1.0 + kPer, t);
        }
        return round2(npv);
    }

    // TIR por periodo (fracción) usando bisección, buscando solo tasas >= 0
    private static double calcularTIRPeriodo(java.util.List<Double> flujos) {
        double low = 0.0;     // 0% por periodo
        double high = 1.0;    // 100% por periodo
        double tol = 1e-7;
        int maxIter = 100;

        // NPV en el extremo inferior
        double npvLow = 0.0;
        for (int t = 0; t < flujos.size(); t++) {
            npvLow += flujos.get(t) / Math.pow(1.0 + low, t);
        }

        for (int iter = 0; iter < maxIter; iter++) {
            double mid = (low + high) / 2.0;

            double npvMid = 0.0;
            for (int t = 0; t < flujos.size(); t++) {
                npvMid += flujos.get(t) / Math.pow(1.0 + mid, t);
            }

            if (Math.abs(npvMid) < tol) {
                return mid;   // raíz encontrada
            }

            // Si NPV(low) y NPV(mid) tienen el mismo signo, movemos low
            if (npvLow * npvMid > 0) {
                low = mid;
                npvLow = npvMid;
            } else {
                high = mid;
            }
        }

        return (low + high) / 2.0;
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
                    costosDTO,
                    sim.getTasaDescuentoAnual(),
                    sim.getBono_Reglas_reglas_id() != null
                            ? sim.getBono_Reglas_reglas_id().getBonoRegla_id()
                            : null
            );

            return crearConCronograma(req);
        }
@Override
    public List<Bonos_reglas> evaluarTechoPropio(int propiedadId, String moneda) {
        Propiedades prop = propRepo.findById(propiedadId)
                .orElseThrow(() -> new IllegalArgumentException("Propiedad no existe"));

        double precio = prop.getPrecioInmueble();
        double ingreso = 0.0;
        if (prop.getClientes_cliente_id() != null) {
            ingreso = prop.getClientes_cliente_id().getIngresosMensuales();
        }

        return bonoRepo.findTechoPropioElegibles(moneda, precio, ingreso);
    }
}