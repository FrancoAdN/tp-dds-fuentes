package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.FachadaProcesadorPdI;
import ar.edu.utn.dds.k3003.facades.dtos.ColeccionDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.facades.dtos.PdIDTO;
import ar.edu.utn.dds.k3003.model.Coleccion;
import ar.edu.utn.dds.k3003.repository.ColeccionRepository;
import ar.edu.utn.dds.k3003.repository.InMemoryColeccionRepo;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.val;

public class Fachada implements FachadaFuente {

  private ColeccionRepository coleccionRepository;

  public Fachada() {
    this.coleccionRepository = new InMemoryColeccionRepo();
  }

  @Override
  public ColeccionDTO agregar(ColeccionDTO coleccionDTO) {
    if (this.coleccionRepository.findById(coleccionDTO.nombre()).isPresent()){
      throw  new IllegalArgumentException(coleccionDTO.nombre() + " ya existe");
    }
    val coleccion = new Coleccion(coleccionDTO.nombre(), coleccionDTO.descripcion());
    this.coleccionRepository.save(coleccion);
    return new ColeccionDTO(coleccion.getNombre(), coleccion.getDescripcion());
  }

  @Override
  public ColeccionDTO buscarColeccionXId(String coleccionId) throws NoSuchElementException {
    val coleccionOptional = this.coleccionRepository.findById(coleccionId);
    if(coleccionOptional.isEmpty()){
      throw  new NoSuchElementException(coleccionId + " no existe");
    }
    val coleccion = coleccionOptional.get();
    return new ColeccionDTO(coleccion.getNombre(),coleccion.getDescripcion());
  }

  @Override
  public HechoDTO agregar(HechoDTO hechoDTO) {
    return null;
  }

  @Override
  public HechoDTO buscarHechoXId(String hechoId) throws NoSuchElementException {
    return null;
  }

  @Override
  public List<HechoDTO> buscarHechosXColeccion(String coleccionId) throws NoSuchElementException {
    return null;
  }

  @Override
  public void setProcesadorPdI(FachadaProcesadorPdI procesador) {

  }

  @Override
  public PdIDTO agregar(PdIDTO pdIDTO) throws IllegalStateException {
    return null;
  }
}
