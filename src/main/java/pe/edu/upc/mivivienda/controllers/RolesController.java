package pe.edu.upc.mivivienda.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.mivivienda.dtos.Entidades_financierasDTO;
import pe.edu.upc.mivivienda.dtos.RolesDTO;
import pe.edu.upc.mivivienda.entities.Entidades_financieras;
import pe.edu.upc.mivivienda.entities.Roles;
import pe.edu.upc.mivivienda.servicesinterfaces.IRolesService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/roles")
public class RolesController {
    @Autowired
    private IRolesService rS;

    @PostMapping
    public void insertar(@RequestBody RolesDTO dto) {
        ModelMapper m=new ModelMapper();
        Roles mn=m.map(dto, Roles.class);
        rS.insert(mn);
    }

    @GetMapping
    public List<RolesDTO> listar() {
        return rS.list().stream().map(x->{
            ModelMapper m=new ModelMapper();
            return m.map(x,RolesDTO.class);
        }).collect(Collectors.toList());
    }

    @PutMapping
    public void modificar(@RequestBody RolesDTO dto) {
        ModelMapper m=new ModelMapper();
        Roles d=m.map(dto, Roles.class);
        rS.update(d);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable("id") Integer id) {
        rS.delete(id);
    }

    @GetMapping("/{id}")
    public RolesDTO listarId(@PathVariable("id") Integer id) {
        ModelMapper m=new ModelMapper();
        RolesDTO dto = m.map(rS.listarId(id), RolesDTO.class);
        return dto;
    }
}
