package pe.edu.upc.mivivienda.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.mivivienda.dtos.ClientesDTO;
import pe.edu.upc.mivivienda.dtos.VerSimulacionesDTO;
import pe.edu.upc.mivivienda.entities.Clientes;
import pe.edu.upc.mivivienda.servicesinterfaces.IClientesService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
public class ClientesController {
    @Autowired
    private IClientesService cS;

    @PostMapping
    public void insertar(@RequestBody ClientesDTO dto) {
        ModelMapper m=new ModelMapper();
        Clientes mn=m.map(dto, Clientes.class);
        cS.insert(mn);
    }

    @GetMapping
    public List<ClientesDTO> listar() {
        return cS.list().stream().map(x->{
            ModelMapper m=new ModelMapper();
            return m.map(x,ClientesDTO.class);
        }).collect(Collectors.toList());
    }

    @PutMapping
    public void modificar(@RequestBody ClientesDTO dto) {
        ModelMapper m=new ModelMapper();
        Clientes d=m.map(dto, Clientes.class);
        cS.update(d);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable("id") Integer id) {
        cS.delete(id);
    }

    @GetMapping("/{id}")
    public ClientesDTO listarId(@PathVariable("id") Integer id) {
        ModelMapper m=new ModelMapper();
        ClientesDTO dto = m.map(cS.listarId(id), ClientesDTO.class);
        return dto;
    }

    @GetMapping("/ver-simulaciones")
    public List<VerSimulacionesDTO> obtenerSimulaciones() {
        List<VerSimulacionesDTO> dtoLista = new ArrayList<>();
        List<String[]> filaLista=cS.getVerSimulaciones();
        for(String[] columna:filaLista) {
            VerSimulacionesDTO dto = new VerSimulacionesDTO();
            dto.setId(Integer.parseInt(columna[0]));
            dto.setNombre(columna[1]);
            dto.setApellido(columna[2]);
            dto.setDni(Integer.parseInt(columna[3]));
            dto.setIngresosMensuales(Double.parseDouble(columna[4]));
            dto.setMoneda(columna[5]);
            dto.setSimulaciones(columna[6]);
            dtoLista.add(dto);
        }

        return dtoLista;
    }
}
