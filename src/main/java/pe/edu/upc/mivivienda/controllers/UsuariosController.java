package pe.edu.upc.mivivienda.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.mivivienda.dtos.UsuariosDTO;
import pe.edu.upc.mivivienda.entities.Usuarios;
import pe.edu.upc.mivivienda.servicesinterfaces.IUsuariosService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UsuariosController {
    @Autowired
    private IUsuariosService uS;

    @PostMapping
    public void insertar(@RequestBody UsuariosDTO dto) {
        ModelMapper m=new ModelMapper();
        Usuarios mn=m.map(dto, Usuarios.class);
        uS.insert(mn);
    }

    @GetMapping
    public List<UsuariosDTO> listar() {
        return uS.list().stream().map(x->{
            ModelMapper m=new ModelMapper();
            return m.map(x,UsuariosDTO.class);
        }).collect(Collectors.toList());
    }

    @PutMapping
    public void modificar(@RequestBody UsuariosDTO dto) {
        ModelMapper m=new ModelMapper();
        Usuarios d=m.map(dto, Usuarios.class);
        uS.update(d);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable("id") Integer id) {
        uS.delete(id);
    }

    @GetMapping("/{id}")
    public UsuariosDTO listarId(@PathVariable("id") Integer id) {
        ModelMapper m=new ModelMapper();
        UsuariosDTO dto = m.map(uS.listarId(id), UsuariosDTO.class);
        return dto;
    }
}
