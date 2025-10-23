package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.app.IFachadaFuente;
import ar.edu.utn.dds.k3003.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.dtos.HechoConPdisDTO;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/hecho")
public class HechoController {
  

  private final IFachadaFuente fachadaFuente;

  @Autowired
  public HechoController(IFachadaFuente fachadaFuente) {
    this.fachadaFuente = fachadaFuente;
  }


  //GET /hecho/{id}
  @GetMapping("/{id}")
  public ResponseEntity<HechoDTO> obtenerHecho(@PathVariable String id) {
    HechoDTO hecho = fachadaFuente.buscarHechoXId(id);
    return ResponseEntity.ok(hecho);
  }

  //POST /hecho
  @PostMapping
  public ResponseEntity<HechoDTO> crearHecho(@RequestBody HechoDTO hechoDTO) {
    System.out.println("Creando hecho: " + hechoDTO);
    HechoDTO creado = fachadaFuente.agregar(hechoDTO);
    return ResponseEntity.ok(creado);
  }

  //PATCH /hecho/{id} {“estado”: “censurado”}
  @PatchMapping("/{id}")
  public ResponseEntity<HechoDTO> actualizarHecho(
    @PathVariable String id,
    @RequestBody HechoDTO hechoDTO
  ) {
    HechoDTO actualizado = fachadaFuente.actualizar(id, hechoDTO);
    return ResponseEntity.ok(actualizado);
  }

  //DELETE /hecho/all
  @DeleteMapping("/all")
  public ResponseEntity<Void> eliminarTodosLosHechos() {
    fachadaFuente.eliminarTodosLosHechos();
    return ResponseEntity.noContent().build();
  }

  //GET /hecho/{id}/pdis
  @GetMapping("/{id}/pdis")
  public ResponseEntity<HechoConPdisDTO> listarPdisDeHecho(@PathVariable String id) {
    var hecho = fachadaFuente.buscarHechoXId(id);
    var pdis = fachadaFuente.pdisDeHecho(id);
    return ResponseEntity.ok(new HechoConPdisDTO(hecho, pdis));
  }

}
