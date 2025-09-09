package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.dtos.EstadoHechoEnum;
import ar.edu.utn.dds.k3003.facades.dtos.CategoriaHechoEnum;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "hechos")
public class Hecho {
    

    public Hecho(
            String nombreColeccion,
            String titulo,
            List<String> etiquetas,
            CategoriaHechoEnum categoria,
            String ubicacion,
            LocalDate fecha,
            String origen,
            EstadoHechoEnum estado) {
        this.nombreColeccion = nombreColeccion;
        this.titulo = titulo;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
        this.origen = origen;
        this.etiquetas = etiquetas;
        this.estado = estado;
    }

    public Hecho() {
        // Default constructor for JPA
    }
    

    
    @Id
    private String id;

    private String nombreColeccion;
    private String titulo;
    @ElementCollection
    private List<String> etiquetas;
    @Enumerated(EnumType.STRING)
    private CategoriaHechoEnum categoria;
    private String ubicacion;
    private LocalDate fecha;
    private String origen;
    @Enumerated(EnumType.STRING)
    private EstadoHechoEnum estado;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreColeccion() {
        return nombreColeccion;
    }

    public void setNombreColeccion(String nombreColeccion) {
        this.nombreColeccion = nombreColeccion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public CategoriaHechoEnum getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaHechoEnum categoria) {
        this.categoria = categoria;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public List<String> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(List<String> etiquetas) {
        this.etiquetas = etiquetas;
    }

    public EstadoHechoEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoHechoEnum estado) {
        this.estado = estado;
    }

    public static String generarId() {
        return UUID.randomUUID().toString();
    }
}
