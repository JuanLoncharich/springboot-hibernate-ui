package com.JuanLoncharich.hibernateui.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import java.math.BigDecimal;


public class RegistroAlimentoDTO {
    private LocalDate fecha;
    private LocalTime horario;
    private String nombreAlimento;
    private Double cantidad; // en gramos
    private Double caloriasTotales;

    public RegistroAlimentoDTO(LocalDate fecha, LocalTime horario, String nombre, Double cantidad, Double caloriasCalculadas) {
        this.fecha = fecha;
        this.horario = horario;
        this.nombreAlimento = nombre;
        this.cantidad = cantidad;
        this.caloriasTotales = caloriasCalculadas;
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