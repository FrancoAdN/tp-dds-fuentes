package ar.edu.utn.dds.k3003.model;

// import lombok.Data;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "colecciones")
public class Coleccion {


    public Coleccion(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Coleccion() {
    }

    @Id
    private String id;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaModificacion;

    public String getNombre() {
        return this.nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public LocalDateTime getFechaModificacion() {
        return this.fechaModificacion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static String generarId() {
        return UUID.randomUUID().toString();
    }

}
