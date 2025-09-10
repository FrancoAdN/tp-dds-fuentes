package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.app.IFachadaFuente;
import ar.edu.utn.dds.k3003.dtos.ColeccionDTO;
import ar.edu.utn.dds.k3003.dtos.HechoDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colecciones")
public class ColeccionController {

    private final IFachadaFuente fachadaFuente;

    @Autowired
    public ColeccionController(IFachadaFuente fachadaFuente) {
        this.fachadaFuente = fachadaFuente;
    }

    //GET /colecciones
    @GetMapping
    public ResponseEntity<List<ColeccionDTO>> listarColecciones() {
        return ResponseEntity.ok(fachadaFuente.colecciones());
    }

    //GET /coleccion/{nombre}
    @GetMapping("/{nombre}")
    public ResponseEntity<ColeccionDTO> obtenerColeccion(@PathVariable String nombre) {
        return ResponseEntity.ok(fachadaFuente.buscarColeccionXId(nombre));
    }

    //POST /coleccion
    @PostMapping
    public ResponseEntity<ColeccionDTO> crearColeccion(@RequestBody ColeccionDTO coleccion) {
        return ResponseEntity.ok(fachadaFuente.agregar(coleccion));
    }

    // GET /coleccion/{nombre}/hechos
    @GetMapping("/{nombre}/hechos")
    public ResponseEntity<List<HechoDTO>> obtenerHechosPorColeccion(@PathVariable String nombre) {
        List<HechoDTO> hechos = fachadaFuente.buscarHechosXColeccion(nombre);
        return ResponseEntity.ok(hechos);
    }
} 