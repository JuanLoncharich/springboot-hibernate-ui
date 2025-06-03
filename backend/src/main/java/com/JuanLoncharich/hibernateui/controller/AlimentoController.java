package com.JuanLoncharich.hibernateui.controller;

import com.JuanLoncharich.hibernateui.dto.AlimentoDTO;
import com.JuanLoncharich.hibernateui.repository.AlimentoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alimentos")
public class AlimentoController {

    private final AlimentoRepository alimentoRepo;

    public AlimentoController(AlimentoRepository alimentoRepo) {
        this.alimentoRepo = alimentoRepo;
    }

    @GetMapping("/buscar-alimentos")
    public List<AlimentoDTO> buscarAlimentos(@RequestParam String termino) {
        return alimentoRepo.findByNombreContainingIgnoreCase(termino);
    }
}