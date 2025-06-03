package com.JuanLoncharich.hibernateui.model;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.*;

@Embeddable
public class ContieneId implements Serializable {
    private Long idComida;
    private Long idAlimento;

    public ContieneId() {}

    public ContieneId(Long idComida, Long idAlimento) {
        this.idComida = idComida;
        this.idAlimento = idAlimento;
    }

    // Getters y Setters o campos públicos
    public Long getIdComida() { return idComida; }
    public void setIdComida(Long idComida) { this.idComida = idComida; }

    public Long getIdAlimento() { return idAlimento; }
    public void setIdAlimento(Long idAlimento) { this.idAlimento = idAlimento; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContieneId)) return false;
        ContieneId that = (ContieneId) o;
        return idComida.equals(that.idComida) && idAlimento.equals(that.idAlimento);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(idComida, idAlimento);
    }
}