package com.JuanLoncharich.hibernateui.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import java.math.BigDecimal;


public class RegistroAlimentoDTO {

    private Long id;
    private LocalDate fecha;
    private LocalTime horario;
    private String nombreAlimento;
    private Double cantidad; // en gramos
    private Double caloriasTotales;
    private Double proteinas;
    private Double carbohidratos;
    private Double grasas;

    public RegistroAlimentoDTO(Long id, LocalDate fecha, LocalTime horario, String nombre,
                               Double cantidad, Double caloriasCalculadas,
                               Double proteinas, Double carbohidratos, Double grasas) {
        this.id = id;
        this.fecha = fecha;
        this.horario = horario;
        this.nombreAlimento = nombre;
        this.cantidad = cantidad;
        this.caloriasTotales = caloriasCalculadas;
        this.proteinas = proteinas;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
    }

    public RegistroAlimentoDTO() {
        // Constructor vacío para uso de JPA
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public String getNombreAlimento() {
        return nombreAlimento;
    }

    public void setNombreAlimento(String nombreAlimento) {
        this.nombreAlimento = nombreAlimento;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Double getCaloriasTotales() {
        return caloriasTotales;
    }

    public void setCaloriasTotales(Double caloriasTotales) {
        this.caloriasTotales = caloriasTotales;
    }
}