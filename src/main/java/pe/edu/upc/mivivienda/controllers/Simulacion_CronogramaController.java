package pe.edu.upc.mivivienda.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.mivivienda.dtos.Simulacion_CronogramaDTO;
import pe.edu.upc.mivivienda.entities.Simulacion_Cronograma;
import pe.edu.upc.mivivienda.servicesinterfaces.Simulacion_CronogramaService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/simulacion_cronograma")
public class Simulacion_CronogramaController{
    @Autowired
    private Simulacion_CronogramaService scS;

    @PostMapping
    public void insertar(@RequestBody Simulacion_CronogramaDTO dto) {
        ModelMapper m=new ModelMapper();
        Simulacion_Cronograma mn=m.map(dto, Simulacion_Cronograma.class);
        scS.insert(mn);
    }

    @GetMapping
    public List<Simulacion_CronogramaDTO> listar() {
        return scS.list().stream().map(x->{
            ModelMapper m=new ModelMapper();
            return m.map(x,Simulacion_CronogramaDTO.class);
        }).collect(Collectors.toList());
    }

    @PutMapping
    public void modificar(@RequestBody Simulacion_CronogramaDTO dto) {
        ModelMapper m=new ModelMapper();
        Simulacion_Cronograma d=m.map(dto, Simulacion_Cronograma.class);
        scS.update(d);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable("id") Integer id) {
        scS.delete(id);
    }

    @GetMapping("/{id}")
    public Simulacion_CronogramaDTO listarId(@PathVariable("id") Integer id) {
        ModelMapper m=new ModelMapper();
        Simulacion_CronogramaDTO dto = m.map(scS.listarId(id), Simulacion_CronogramaDTO.class);
        return dto;
    }
}
