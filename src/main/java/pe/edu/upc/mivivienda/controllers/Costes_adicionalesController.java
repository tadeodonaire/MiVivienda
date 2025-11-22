package pe.edu.upc.mivivienda.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.mivivienda.dtos.Costes_adicionalesDTO;
import pe.edu.upc.mivivienda.entities.Costes_adicionales;
import pe.edu.upc.mivivienda.servicesinterfaces.ICostes_adicionalesService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/costes_adicionales")
public class Costes_adicionalesController {

    @Autowired
    private ICostes_adicionalesService caS;

    @PostMapping
    public void insertar(@RequestBody Costes_adicionalesDTO dto) {
        ModelMapper m = new ModelMapper();
        Costes_adicionales entity = m.map(dto, Costes_adicionales.class);
        caS.insert(entity);
    }

    @GetMapping
    public List<Costes_adicionalesDTO> listar() {
        return caS.list().stream().map(x -> {
            ModelMapper m = new ModelMapper();
            return m.map(x, Costes_adicionalesDTO.class);
        }).collect(Collectors.toList());
    }

    @PutMapping
    public void modificar(@RequestBody Costes_adicionalesDTO dto) {
        ModelMapper m = new ModelMapper();
        Costes_adicionales entity = m.map(dto, Costes_adicionales.class);
        caS.update(entity);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable("id") Integer id) {
        caS.delete(id);
    }

    @GetMapping("/{id}")
    public Costes_adicionalesDTO listarId(@PathVariable("id") Integer id) {
        ModelMapper m = new ModelMapper();
        Costes_adicionalesDTO dto = m.map(caS.listarId(id), Costes_adicionalesDTO.class);
        return dto;
    }
}