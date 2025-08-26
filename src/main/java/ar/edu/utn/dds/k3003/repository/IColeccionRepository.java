package ar.edu.utn.dds.k3003.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.edu.utn.dds.k3003.model.Coleccion;


public interface IColeccionRepository extends JpaRepository<Coleccion, String> {
    // JpaRepository provides basic CRUD operations and pagination
    // Additional custom methods can be defined here if needed

    List<Coleccion> findByNombre(String nombre); // Example of a custom query method

    Optional<Coleccion> findById(String id); // Find by ID method, assuming ID is a String
    
}