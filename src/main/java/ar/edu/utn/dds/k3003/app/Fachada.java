package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaProcesadorPdI;
import ar.edu.utn.dds.k3003.facades.dtos.ColeccionDTO;
import ar.edu.utn.dds.k3003.facades.dtos.PdIDTO;
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
import org.springframework.stereotype.Service;

@Service
public class Fachada implements IFachadaFuente {

	@Autowired
	private IColeccionRepository coleccionRepository;
	@Autowired
	private IHechoRepository hechoRepository;

	private FachadaProcesadorPdI procesadorPdI;

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
		this.coleccionRepository.save(coleccion);
		return new ColeccionDTO(coleccion.getNombre(), coleccion.getDescripcion());
	}

	@Override
	public ColeccionDTO buscarColeccionXId(String id) {
		Optional<Coleccion> coleccionOptional = this.coleccionRepository.findById(id).stream().findFirst();
		if (coleccionOptional.isEmpty()) {
			throw new NoSuchElementException(id + " no existe");
		}
		Coleccion coleccion = coleccionOptional.get();
		return new ColeccionDTO(coleccion.getNombre(), coleccion.getDescripcion());
	}

	@Override
	public HechoDTO agregar(HechoDTO hechoDTO) {
		ColeccionDTO coleccionDTO = this.buscarColeccionXId(hechoDTO.nombre_coleccion());
		Hecho hecho =
				new Hecho(
						coleccionDTO.nombre(),
						hechoDTO.titulo(),
						hechoDTO.etiquetas(),
						hechoDTO.categoria(),
						hechoDTO.ubicacion(),
						hechoDTO.fecha(),
						hechoDTO.origen(),
                        hechoDTO.estado());
		hecho.setId(Hecho.generarId());
		System.err.println("Hecho ID generado: " + hecho.getId());

		this.hechoRepository.save(hecho);

		return new HechoDTO(
				hecho.getNombreColeccion(),
				hecho.getTitulo(),
				hecho.getEtiquetas(),
				hecho.getCategoria(),
				hecho.getUbicacion(),
				hecho.getFecha(),
				hecho.getOrigen(),
        hecho.getEstado());
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
                hecho.getEstado());
	}

	@Override
	public List<HechoDTO> buscarHechosXColeccion(String coleccionId) {
		this.buscarColeccionXId(coleccionId);

    List<Hecho> hechos = this.hechoRepository.findByNombreColeccionAndEstadoNot(coleccionId, EstadoHechoEnum.CENSURADO);

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
										hecho.getEstado()))
				.toList();
	}

	@Override
	public void setProcesadorPdI(FachadaProcesadorPdI procesador) {
		this.procesadorPdI = procesador;
	}

	@Override
	public PdIDTO agregar(PdIDTO pdIDTO) {
		this.buscarHechoXId(pdIDTO.hechoId());
		PdIDTO resultado = this.procesadorPdI.procesar(pdIDTO);
		return resultado;
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
			hecho.getEstado());
}
    

	@Override
	public List<ColeccionDTO> colecciones() {
		return this.coleccionRepository.findAll().stream()
			.map(coleccion -> new ColeccionDTO(coleccion.getNombre(), coleccion.getDescripcion()))
			.toList();
	}
}

