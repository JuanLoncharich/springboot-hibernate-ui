package com.JuanLoncharich.hibernateui.repository;

import com.JuanLoncharich.hibernateui.dto.RegistroAlimentoDTO;
import com.JuanLoncharich.hibernateui.model.Contiene;
import com.JuanLoncharich.hibernateui.model.ContieneId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import jakarta.persistence.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface ContieneRepository extends JpaRepository<Contiene, ContieneId> {

    @Query("SELECT new com.JuanLoncharich.hibernateui.dto.RegistroAlimentoDTO(" +
            "r.fecha, r.horario, a.nombre, c.cantidad, a.calorias * c.cantidad / 100) " +
            "FROM Contiene c " +
            "JOIN c.registro r " +
            "JOIN c.alimento a " +
            "WHERE r.usuario.id = :usuarioId")
    List<RegistroAlimentoDTO> findRegistrosConAlimentos(@Param("usuarioId") Long usuarioId);


}