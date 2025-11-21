package pe.edu.upc.mivivienda.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.mivivienda.dtos.Bonos_reglasDTO;
import pe.edu.upc.mivivienda.dtos.Entidades_financierasDTO;
import pe.edu.upc.mivivienda.entities.Bonos_reglas;
import pe.edu.upc.mivivienda.servicesinterfaces.IBonos_reglasService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bonos_reglas")
public class Bonos_reglasController {
    @Autowired
    private IBonos_reglasService brS;

    @PostMapping
    public void insertar(@RequestBody Bonos_reglasDTO dto) {
        ModelMapper m=new ModelMapper();
        Bonos_reglas mn=m.map(dto, Bonos_reglas.class);
        brS.insert(mn);
    }

    @GetMapping
    public List<Bonos_reglasDTO> listar() {
        return brS.list().stream().map(x->{
            ModelMapper m=new ModelMapper();
            return m.map(x,Bonos_reglasDTO.class);
        }).collect(Collectors.toList());
    }

    @PutMapping
    public void modificar(@RequestBody Bonos_reglasDTO dto) {
        ModelMapper m=new ModelMapper();
        Bonos_reglas d=m.map(dto, Bonos_reglas.class);
        brS.update(d);
    }

    @DeleteMapping("/{id}")
        public void eliminar(@PathVariable("id") Integer id) {
        brS.delete(id);
    }

    @GetMapping("/{id}")
    public Entidades_financierasDTO listarId(@PathVariable("id") Integer id) {
        ModelMapper m=new ModelMapper();
        Entidades_financierasDTO dto = m.map(brS.listarId(id), Entidades_financierasDTO.class);
        return dto;
    }
}
