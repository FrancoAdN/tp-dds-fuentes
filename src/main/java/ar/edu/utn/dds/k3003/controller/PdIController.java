package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.app.IFachadaFuente;
import ar.edu.utn.dds.k3003.dtos.PdIDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pdis")
public class PdIController {

  private final IFachadaFuente fachadaFuente;

  @Autowired
  public PdIController(IFachadaFuente fachadaFuente) {
    this.fachadaFuente = fachadaFuente;
  }

  @PostMapping
  public ResponseEntity<PdIDTO> crearPdi(@RequestBody PdIDTO pdIDTO) {
    PdIDTO creado = this.fachadaFuente.agregar(pdIDTO);
    return ResponseEntity.ok(creado);
  }
}


