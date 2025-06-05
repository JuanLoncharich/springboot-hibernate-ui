package com.JuanLoncharich.hibernateui.controller;

import com.JuanLoncharich.hibernateui.dto.AlimentoDTO;
import com.JuanLoncharich.hibernateui.repository.AlimentoRepository;
import com.JuanLoncharich.hibernateui.model.Alimento;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
