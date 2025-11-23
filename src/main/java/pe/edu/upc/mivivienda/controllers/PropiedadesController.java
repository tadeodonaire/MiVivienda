package pe.edu.upc.mivivienda.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.mivivienda.dtos.PropiedadesDTO;
import pe.edu.upc.mivivienda.entities.Propiedades;
import pe.edu.upc.mivivienda.servicesinterfaces.IPropiedadesService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/propiedades")
public class PropiedadesController {

    @Autowired
    private IPropiedadesService pS;

    @PostMapping
    public void insertar(@RequestBody PropiedadesDTO dto) {
        ModelMapper m=new ModelMapper();
        Propiedades mn=m.map(dto, Propiedades.class);
        pS.insert(mn);
    }

    @GetMapping
    public List<PropiedadesDTO> listar() {
        return pS.list().stream().map(x->{
            ModelMapper m=new ModelMapper();
            return m.map(x,PropiedadesDTO.class);
        }).collect(Collectors.toList());
    }

    @PutMapping
    public void modificar(@RequestBody PropiedadesDTO dto) {
        ModelMapper m=new ModelMapper();
        Propiedades d=m.map(dto, Propiedades.class);
        pS.update(d);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable("id") Integer id) {
        pS.delete(id);
    }

    @GetMapping("/{id}")
    public PropiedadesDTO listarId(@PathVariable("id") Integer id) {
        ModelMapper m=new ModelMapper();
        PropiedadesDTO dto = m.map(pS.listarId(id), PropiedadesDTO.class);
        return dto;
    }
}
