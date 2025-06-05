package com.JuanLoncharich.hibernateui.repository;

import com.JuanLoncharich.hibernateui.model.Alimento;
import com.JuanLoncharich.hibernateui.dto.AlimentoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

import java.util.List;

public interface AlimentoRepository extends JpaRepository<Alimento, Long> {

    @Query("SELECT new com.JuanLoncharich.hibernateui.dto.AlimentoDTO(a.id, a.nombre, a.calorias, a.proteinas, a.carbohidratos, a.grasas) " +
            "FROM Alimento a " +
            "WHERE LOWER(a.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<AlimentoDTO> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);

    Optional<Alimento> findByNombre(String nombre);

}
