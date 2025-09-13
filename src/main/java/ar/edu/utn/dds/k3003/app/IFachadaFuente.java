package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.dtos.ColeccionDTO;
import ar.edu.utn.dds.k3003.dtos.PdIDTO;
import java.util.List;
import java.util.NoSuchElementException;

public interface IFachadaFuente {


  ColeccionDTO agregar(ColeccionDTO coleccionDTO);

  // ColeccionDTO buscarColeccionXId(String coleccionId) throws NoSuchElementException;

  ColeccionDTO buscarColeccionXNombre(String nombre) throws NoSuchElementException;

  HechoDTO agregar(HechoDTO hechoDTO);
  HechoDTO buscarHechoXId(String hechoId) throws NoSuchElementException;

  List<HechoDTO> buscarHechosXColeccion(String coleccionId) throws NoSuchElementException;

  List<HechoDTO> buscarHechosXColeccionPorNombre(String nombreColeccion) throws NoSuchElementException;


  PdIDTO agregar(PdIDTO pdIDTO) throws IllegalStateException;

  List<PdIDTO> pdisDeHecho(String hechoId) throws NoSuchElementException, IllegalStateException;

  List<ColeccionDTO> colecciones();


  HechoDTO actualizar(String hechoId, HechoDTO hechoDTO) throws NoSuchElementException;

  void eliminarTodosLosHechos();

  void eliminarTodasLasColecciones();
}