package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.app.IFachadaFuente;
import ar.edu.utn.dds.k3003.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.app.MetricsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/hecho")
public class HechoController {
  

  private final IFachadaFuente fachadaFuente;
  private final MetricsService metricsService;

  @Autowired
  public HechoController(IFachadaFuente fachadaFuente, MetricsService metricsService) {
    this.fachadaFuente = fachadaFuente;
    this.metricsService = metricsService;
  }


  //GET /hecho/{id}
  @GetMapping("/{id}")
  public ResponseEntity<HechoDTO> obtenerHecho(@PathVariable String id) {
    String path = "/api/hecho/{id}";
    try {
      HechoDTO hecho = fachadaFuente.buscarHechoXId(id);
      metricsService.markSuccess(path, "GET");
      return ResponseEntity.ok(hecho);
    } catch (RuntimeException ex) {
      metricsService.markError(path, "GET");
      throw ex;
    }
  }

  //POST /hecho
  @PostMapping
  public ResponseEntity<HechoDTO> crearHecho(@RequestBody HechoDTO hechoDTO) {
    String path = "/api/hecho";
    try {
      System.out.println("Creando hecho: " + hechoDTO);
      HechoDTO creado = fachadaFuente.agregar(hechoDTO);
      metricsService.markSuccess(path, "POST");
      return ResponseEntity.ok(creado);
    } catch (RuntimeException ex) {
      metricsService.markError(path, "POST");
      throw ex;
    }
  }

  //PATCH /hecho/{id} {“estado”: “censurado”}
  @PatchMapping("/{id}")
  public ResponseEntity<HechoDTO> actualizarHecho(
    @PathVariable String id,
    @RequestBody HechoDTO hechoDTO
  ) {
    String path = "/api/hecho/{id}";
    try {
      HechoDTO actualizado = fachadaFuente.actualizar(id, hechoDTO);
      metricsService.markSuccess(path, "PATCH");
      return ResponseEntity.ok(actualizado);
    } catch (RuntimeException ex) {
      metricsService.markError(path, "PATCH");
      throw ex;
    }
  }

}
