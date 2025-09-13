package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.dtos.ColeccionDTO;
import ar.edu.utn.dds.k3003.dtos.PdIDTO;
import ar.edu.utn.dds.k3003.model.Coleccion;
import ar.edu.utn.dds.k3003.dtos.EstadoHechoEnum;
import ar.edu.utn.dds.k3003.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.model.Hecho;
import ar.edu.utn.dds.k3003.repository.IColeccionRepository;
import ar.edu.utn.dds.k3003.repository.IHechoRepository;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import retrofit2.Response;

import java.io.IOException;

@Service
public class Fachada implements IFachadaFuente {

	@Autowired
	private IColeccionRepository coleccionRepository;
	@Autowired
	private IHechoRepository hechoRepository;

	@Autowired
	private ProcesadorPdIClient procesadorPdIClient;

	public Fachada() {
		// Default constructor for Spring
	}

	@Override
	public ColeccionDTO agregar(ColeccionDTO coleccionDTO) {
		Optional<Coleccion> coleccionExistente = this.coleccionRepository.findByNombre(coleccionDTO.nombre()).stream().findFirst();
		if (coleccionExistente.isPresent()) {
			throw new IllegalArgumentException(coleccionDTO.nombre() + " ya existe");
		}
		Coleccion coleccion = new Coleccion(coleccionDTO.nombre(), coleccionDTO.descripcion());
		coleccion.setId(Coleccion.generarId());
		this.coleccionRepository.save(coleccion);
		return new ColeccionDTO(coleccion.getNombre(), coleccion.getDescripcion(), coleccion.getId());
	}

	// @Override
	// public ColeccionDTO buscarColeccionXId(String id) {
	// 	System.out.println("Buscando coleccion con ID: " + id);
	// 	Optional<Coleccion> coleccionOptional = this.coleccionRepository.findById(id).stream().findFirst();
	// 	System.out.println("ColeccionOptional: " + coleccionOptional);
  //   if (coleccionOptional.isEmpty()) {
	// 		throw new NoSuchElementException(id + " no existe");
	// 	}
	// 	Coleccion coleccion = coleccionOptional.get();
	// 	System.out.println("Coleccion: " + coleccion);
	// 	return new ColeccionDTO(coleccion.getNombre(), coleccion.getDescripcion(), coleccion.getId());
	// }

	@Override
	public ColeccionDTO buscarColeccionXNombre(String nombre) {
		Optional<Coleccion> coleccionOptional = this.coleccionRepository.findByNombre(nombre).stream().findFirst();
		if (coleccionOptional.isEmpty()) {
			throw new NoSuchElementException(nombre + " no existe");
		}
		Coleccion coleccion = coleccionOptional.get();
		return new ColeccionDTO(coleccion.getNombre(), coleccion.getDescripcion(), coleccion.getId());
	}

	@Override
	public HechoDTO agregar(HechoDTO hechoDTO) {
		ColeccionDTO coleccionDTO = this.buscarColeccionXNombre(hechoDTO.nombre_coleccion());
    System.out.println("ColeccionDTO: " + coleccionDTO);
		Hecho hecho =
				new Hecho(
						hechoDTO.nombre_coleccion(),
						hechoDTO.titulo(),
						hechoDTO.etiquetas(),
						hechoDTO.categoria(),
						hechoDTO.ubicacion(),
						hechoDTO.fecha(),
						hechoDTO.origen(),
                        hechoDTO.estado());
        System.out.println("Hecho: " + hecho);
		hecho.setId(Hecho.generarId());
		System.err.println("Hecho ID generado: " + hecho.getId());

        System.out.println("Hecho ID generado: " + hecho.getId());

		this.hechoRepository.save(hecho);

		return new HechoDTO(
				hecho.getNombreColeccion(),
				hecho.getTitulo(),
				hecho.getEtiquetas(),
				hecho.getCategoria(),
				hecho.getUbicacion(),
				hecho.getFecha(),
				hecho.getOrigen(),
        hecho.getEstado(),
        hecho.getId());
	}

	@Override
	public HechoDTO buscarHechoXId(String hechoId) {
		Optional<Hecho> hechoOptional = this.hechoRepository.findByIdAndEstadoNot(hechoId, EstadoHechoEnum.CENSURADO);

		if (hechoOptional.isEmpty()) {
			throw new NoSuchElementException("Hecho con ID " + hechoId + " no encontrado.");
		}

		Hecho hecho = hechoOptional.get();

		return new HechoDTO(
				hecho.getNombreColeccion(),
				hecho.getTitulo(),
				hecho.getEtiquetas(),
				hecho.getCategoria(),
				hecho.getUbicacion(),
				hecho.getFecha(),
				hecho.getOrigen(),
                hecho.getEstado(),
                hecho.getId());
	}

	@Override
	public List<HechoDTO> buscarHechosXColeccion(String nombreColeccion) {
		this.buscarColeccionXNombre(nombreColeccion);

    List<Hecho> hechos = this.hechoRepository.findByNombreColeccionAndEstadoNot(nombreColeccion, EstadoHechoEnum.CENSURADO);

		return hechos.stream()
				.map(
						hecho -> 
								new HechoDTO(
										hecho.getNombreColeccion(),
										hecho.getTitulo(),
										hecho.getEtiquetas(),
										hecho.getCategoria(),
										hecho.getUbicacion(),
										hecho.getFecha(),
										hecho.getOrigen(),
										hecho.getEstado(),
										hecho.getId()))
				.toList();
	}

	@Override
	public List<HechoDTO> buscarHechosXColeccionPorNombre(String nombreColeccion) {
		this.buscarColeccionXNombre(nombreColeccion);

		List<Hecho> hechos = this.hechoRepository.findByNombreColeccionAndEstadoNot(nombreColeccion, EstadoHechoEnum.CENSURADO);

		return hechos.stream()
				.map(
						hecho ->
								new HechoDTO(
										hecho.getNombreColeccion(),
										hecho.getTitulo(),
										hecho.getEtiquetas(),
										hecho.getCategoria(),
										hecho.getUbicacion(),
										hecho.getFecha(),
										hecho.getOrigen(),
										hecho.getEstado(),
										hecho.getId()))
				.toList();
	}

	@Override
	public PdIDTO agregar(PdIDTO pdIDTO) throws IllegalStateException {
		this.buscarHechoXId(pdIDTO.hechoId());
		System.out.println("PdIDTO: " + pdIDTO);
		try {
			Response<PdIDTO> response = this.procesadorPdIClient.procesar(pdIDTO).execute();
      System.out.println("Response: " + response);
			if (!response.isSuccessful() || response.body() == null) {
        if (response.code() == 422) {
          throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Hecho no inactivo");
        }
				throw new IllegalStateException("Error al procesar PdI: " + response.code());
			}
			return response.body();
		} catch (IOException e) {
      throw new IllegalStateException("Fallo al comunicarse con el Procesador de PdI", e);
		}
	}

	@Override
	public List<PdIDTO> pdisDeHecho(String hechoId) throws NoSuchElementException, IllegalStateException {
		this.buscarHechoXId(hechoId);
		try {
			Response<java.util.List<PdIDTO>> response = this.procesadorPdIClient.listarPorHecho(hechoId).execute();
			if (!response.isSuccessful() || response.body() == null) {
				throw new IllegalStateException("Error al obtener PdIs: " + (response != null ? response.code() : "sin respuesta"));
			}
			return response.body();
		} catch (IOException e) {
			throw new IllegalStateException("Fallo al comunicarse con el Procesador de PdI", e);
		}
	}

@Override
public HechoDTO actualizar(String hechoId, HechoDTO hechoDTO) {
	Optional<Hecho> hechoOptional = this.hechoRepository.findById(hechoId);
	if (hechoOptional.isEmpty()) {
		throw new NoSuchElementException("Hecho con ID " + hechoId + " no encontrado.");
	}

	Hecho hecho = hechoOptional.get();

	if (hechoDTO.titulo() != null) hecho.setTitulo(hechoDTO.titulo());
	if (hechoDTO.etiquetas() != null) hecho.setEtiquetas(hechoDTO.etiquetas());
	if (hechoDTO.categoria() != null) hecho.setCategoria(hechoDTO.categoria());
	if (hechoDTO.ubicacion() != null) hecho.setUbicacion(hechoDTO.ubicacion());
	if (hechoDTO.fecha() != null) hecho.setFecha(hechoDTO.fecha());
	if (hechoDTO.origen() != null) hecho.setOrigen(hechoDTO.origen());
	if (hechoDTO.estado() != null) hecho.setEstado(hechoDTO.estado());

	this.hechoRepository.save(hecho);

	return new HechoDTO(
			hecho.getNombreColeccion(),
			hecho.getTitulo(),
			hecho.getEtiquetas(),
			hecho.getCategoria(),
			hecho.getUbicacion(),
			hecho.getFecha(),
			hecho.getOrigen(),
			hecho.getEstado(),
			hecho.getId());
}
    

	@Override
	public List<ColeccionDTO> colecciones() {
		return this.coleccionRepository.findAll().stream()
			.map(coleccion -> new ColeccionDTO(coleccion.getNombre(), coleccion.getDescripcion(), coleccion.getId()))
			.toList();
	}

	@Override
	public void eliminarTodosLosHechos() {
		this.hechoRepository.deleteAll();
	}

	@Override
	public void eliminarTodasLasColecciones() {
		this.coleccionRepository.deleteAll();
	}
}

