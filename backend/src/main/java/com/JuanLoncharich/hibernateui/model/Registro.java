package com.JuanLoncharich.hibernateui.model;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;

import com.JuanLoncharich.hibernateui.model.User;

@Entity
@Table(name = "registro")
public class Registro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comida")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario") // debe coincidir con el nombre real de la columna en la DB
    private User usuario;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "horario")
    private LocalTime horario;

    @OneToMany(mappedBy = "registro")
    private List<Contiene> contieneList = new ArrayList<>();

    public Long getIdUsuario() {
        return usuario != null ? usuario.getId() : null;
    }

    public void setIdUsuario(Long idUsuario) {
        if (usuario == null) {
            usuario = new User();
        }
        usuario.setId(idUsuario);
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

    public List<Contiene> getContieneList() {
        return contieneList;
    }

    public void setContieneList(List<Contiene> contieneList) {
        this.contieneList = contieneList;
    }
}