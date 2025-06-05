package com.JuanLoncharich.hibernateui.dto;

import java.time.LocalDate;
import java.util.List;

public class RegistroConAlimentosDTO {
    public Long id;
    public LocalDate fecha;
    public String horario;
    public Long idUsuario;
    public List<AlimentoCantidadDTO> alimentos;
}