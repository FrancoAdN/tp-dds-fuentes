package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.app.IFachadaFuente;
import ar.edu.utn.dds.k3003.dtos.ColeccionDTO;
import ar.edu.utn.dds.k3003.dtos.HechoDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import ar.edu.utn.dds.k3003.app.MetricsService;

@RestController
@RequestMapping("/api/colecciones")
public class ColeccionController {

    private final IFachadaFuente fachadaFuente;
    private final MetricsService metricsService;

    @Autowired
    public ColeccionController(IFachadaFuente fachadaFuente, MetricsService metricsService) {
        this.fachadaFuente = fachadaFuente;
        this.metricsService = metricsService;
    }

    //GET /colecciones
    @GetMapping
    public ResponseEntity<List<ColeccionDTO>> listarColecciones() {
        try {
            List<ColeccionDTO> result = fachadaFuente.colecciones();
            metricsService.markSuccess("/api/colecciones", "GET");
            return ResponseEntity.ok(result);
        } catch (RuntimeException ex) {
            metricsService.markError("/api/colecciones", "GET");
            throw ex;
        }
    }

    //GET /coleccion/{nombre}
    @GetMapping("/{nombre}")
    public ResponseEntity<ColeccionDTO> obtenerColeccion(@PathVariable String nombre) {
        String path = "/api/colecciones/{nombre}";
        try {
            ColeccionDTO result = fachadaFuente.buscarColeccionXId(nombre);
            metricsService.markSuccess(path, "GET");
            return ResponseEntity.ok(result);
        } catch (RuntimeException ex) {
            metricsService.markError(path, "GET");
            throw ex;
        }
    }

    //POST /coleccion
    @PostMapping
    public ResponseEntity<ColeccionDTO> crearColeccion(@RequestBody ColeccionDTO coleccion) {
        String path = "/api/colecciones";
        try {
            ColeccionDTO result = fachadaFuente.agregar(coleccion);
            metricsService.markSuccess(path, "POST");
            return ResponseEntity.ok(result);
        } catch (RuntimeException ex) {
            metricsService.markError(path, "POST");
            throw ex;
        }
    }

    // GET /coleccion/{nombre}/hechos
    @GetMapping("/{nombre}/hechos")
    public ResponseEntity<List<HechoDTO>> obtenerHechosPorColeccion(@PathVariable String nombre) {
        String path = "/api/colecciones/{nombre}/hechos";
        try {
            List<HechoDTO> hechos = fachadaFuente.buscarHechosXColeccion(nombre);
            metricsService.markSuccess(path, "GET");
            return ResponseEntity.ok(hechos);
        } catch (RuntimeException ex) {
            metricsService.markError(path, "GET");
            throw ex;
        }
    }
} 