package pe.edu.upc.mivivienda.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.mivivienda.dtos.Entidades_financierasDTO;
import pe.edu.upc.mivivienda.entities.Entidades_financieras;
import pe.edu.upc.mivivienda.servicesinterfaces.IEntidades_financierasService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/entidades_financieras")
public class Entidades_financierasController {
    @Autowired
    private IEntidades_financierasService efS;

    @PostMapping
    public void insertar(@RequestBody Entidades_financierasDTO dto) {
        ModelMapper m=new ModelMapper();
        Entidades_financieras mn=m.map(dto, Entidades_financieras.class);
        efS.insert(mn);
    }

    @GetMapping
    public List<Entidades_financierasDTO> listar() {
        return efS.list().stream().map(x->{
            ModelMapper m=new ModelMapper();
            return m.map(x,Entidades_financierasDTO.class);
        }).collect(Collectors.toList());
    }

    @PutMapping
    public void modificar(@RequestBody Entidades_financierasDTO dto) {
        ModelMapper m=new ModelMapper();
        Entidades_financieras d=m.map(dto, Entidades_financieras.class);
        efS.update(d);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable("id") Integer id) {
        efS.delete(id);
    }

    @GetMapping("/{id}")
    public Entidades_financierasDTO listarId(@PathVariable("id") Integer id) {
        ModelMapper m=new ModelMapper();
        Entidades_financierasDTO dto = m.map(efS.listarId(id), Entidades_financierasDTO.class);
        return dto;
    }
}
