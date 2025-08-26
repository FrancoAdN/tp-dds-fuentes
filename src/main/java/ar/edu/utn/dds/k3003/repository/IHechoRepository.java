package ar.edu.utn.dds.k3003.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.edu.utn.dds.k3003.dtos.EstadoHechoEnum;
import ar.edu.utn.dds.k3003.model.Hecho;


public interface IHechoRepository extends JpaRepository<Hecho, String> {
    // JpaRepository provides basic CRUD operations and pagination
    // Additional custom methods can be defined here if needed


    Optional<Hecho> findByIdAndEstadoNot(String id, EstadoHechoEnum estado);

    List<Hecho> findByNombreColeccionAndEstadoNot(String nombreColeccion, EstadoHechoEnum estado);

}