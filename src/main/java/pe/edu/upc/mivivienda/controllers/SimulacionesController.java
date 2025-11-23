package pe.edu.upc.mivivienda.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.mivivienda.dtos.SimulacionesDTO;
import pe.edu.upc.mivivienda.entities.Simulaciones;
import pe.edu.upc.mivivienda.servicesinterfaces.ISimulacionesService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/simulaciones")
public class SimulacionesController {
    @Autowired
    private ISimulacionesService sS;

    @PostMapping
    public void insertar(@RequestBody SimulacionesDTO dto) {
        ModelMapper m=new ModelMapper();
        Simulaciones mn=m.map(dto, Simulaciones.class);
        sS.insert(mn);
    }

    @GetMapping
    public List<SimulacionesDTO> listar() {
        return sS.list().stream().map(x->{
            ModelMapper m=new ModelMapper();
            return m.map(x,SimulacionesDTO.class);
        }).collect(Collectors.toList());
    }

    @PutMapping
    public void modificar(@RequestBody SimulacionesDTO dto) {
        ModelMapper m=new ModelMapper();
        Simulaciones d=m.map(dto, Simulaciones.class);
        sS.update(d);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable("id") Integer id) {
        sS.delete(id);
    }

    @GetMapping("/{id}")
    public SimulacionesDTO listarId(@PathVariable("id") Integer id) {
        ModelMapper m=new ModelMapper();
        SimulacionesDTO dto = m.map(sS.listarId(id), SimulacionesDTO.class);
        return dto;
    }
}
