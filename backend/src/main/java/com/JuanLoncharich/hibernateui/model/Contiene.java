package com.JuanLoncharich.hibernateui.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import com.JuanLoncharich.hibernateui.model.Registro;

@Entity
@Table(name = "contiene")
public class Contiene {

    @EmbeddedId
    private ContieneId id;

    private String nombreAlimento;
    private Double calorias;
    private Double proteinas;
    private Double carbohidratos;
    private Double grasas;
    private Double cantidad;

    public ContieneId getId() {
        return id;
    }

    public void setId(ContieneId id) {
        this.id = id;
    }

    public String getNombreAlimento() {
        return nombreAlimento;
    }

    public void setNombreAlimento(String nombreAlimento) {
        this.nombreAlimento = nombreAlimento;
    }

    public Double getCalorias() {
        return calorias;
    }

    public void setCalorias(Double calorias) {
        this.calorias = calorias;
    }

    public Double getProteinas() {
        return proteinas;
    }

    public void setProteinas(Double proteinas) {
        this.proteinas = proteinas;
    }

    public Double getCarbohidratos() {
        return carbohidratos;
    }

    public void setCarbohidratos(Double carbohidratos) {
        this.carbohidratos = carbohidratos;
    }

    public Double getGrasas() {
        return grasas;
    }

    public void setGrasas(Double grasas) {
        this.grasas = grasas;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Registro getRegistro() {
        return registro;
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }

    public Alimento getAlimento() {
        return alimento;
    }

    public void setAlimento(Alimento alimento) {
        this.alimento = alimento;
    }

    @MapsId("idComida")
    @ManyToOne
    @JoinColumn(name = "id_comida", nullable = false)
    private Registro registro;

    @MapsId("idAlimento")
    @ManyToOne
    @JoinColumn(name = "id_alimento", nullable = true)
    private Alimento alimento;
}