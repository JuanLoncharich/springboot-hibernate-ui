package com.JuanLoncharich.hibernateui.repository;

import com.JuanLoncharich.hibernateui.model.Registro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistroRepository extends JpaRepository<Registro, Long> {
    List<Registro> findByUsuarioId(Long idUsuario);
}