package com.JuanLoncharich.hibernateui.service;

import com.JuanLoncharich.hibernateui.dto.RegistroConAlimentosDTO;
import com.JuanLoncharich.hibernateui.dto.AlimentoCantidadDTO;
import com.JuanLoncharich.hibernateui.model.Alimento;
import com.JuanLoncharich.hibernateui.model.Contiene;
import com.JuanLoncharich.hibernateui.model.ContieneId;
import com.JuanLoncharich.hibernateui.model.Registro;
import com.JuanLoncharich.hibernateui.repository.AlimentoRepository;
import com.JuanLoncharich.hibernateui.repository.ContieneRepository;
import com.JuanLoncharich.hibernateui.repository.RegistroRepository;
import com.JuanLoncharich.hibernateui.dto.RegistroAlimentoDTO;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class RegistroService {

    private final RegistroRepository registroRepo;
    private final AlimentoRepository alimentoRepo;
    private final ContieneRepository contieneRepo;

    public RegistroService(
            RegistroRepository registroRepo,
            AlimentoRepository alimentoRepo,
            ContieneRepository contieneRepo) {
        this.registroRepo = registroRepo;
        this.alimentoRepo = alimentoRepo;
        this.contieneRepo = contieneRepo;
    }

    public Registro crearRegistroConAlimentos(RegistroConAlimentosDTO dto) {
        if (dto.alimentos == null || dto.alimentos.isEmpty()) {
            throw new RuntimeException("Debe haber al menos un alimento");
        }

        Registro registro = new Registro();
        registro.setFecha(dto.fecha);
        registro.setHorario(LocalTime.parse(dto.horario)); // formato "HH:mm"
        registro.setIdUsuario(dto.idUsuario);

        Registro savedRegistro = registroRepo.save(registro);

        for (AlimentoCantidadDTO alimentoDTO : dto.alimentos) {
            Alimento alimento = alimentoRepo.findById(alimentoDTO.id)
                    .orElseThrow(() -> new RuntimeException("Alimento no encontrado"));

            Contiene contiene = new Contiene();

            ContieneId id = new ContieneId();
            id.setIdComida(savedRegistro.getId());
            id.setIdAlimento(alimento.getId());

            contiene.setId(id);
            contiene.setCantidad(alimentoDTO.cantidad);
            contiene.setRegistro(savedRegistro);
            contiene.setAlimento(alimento);

            contieneRepo.save(contiene);
        }

        return savedRegistro;
    }
    public List<RegistroAlimentoDTO> getRegistrosConAlimentos(Long usuarioId) {
        return contieneRepo.findRegistrosConAlimentos(usuarioId);
    }
}