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

import jakarta.transaction.Transactional;
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

    public boolean eliminarRegistro(Long id) {
        if (!registroRepo.existsById(id)) {
            return false;
        }
        contieneRepo.deleteByRegistroId(id);
        registroRepo.deleteById(id);
        return true;
    }


    public List<RegistroAlimentoDTO> getRegistrosConAlimentos(Long usuarioId) {
        return contieneRepo.findRegistrosConAlimentos(usuarioId);
    }

    @Transactional
    public Registro actualizarRegistroConAlimentos(RegistroConAlimentosDTO dto) {
        Registro registro = registroRepo.findById(dto.id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));

        registro.setFecha(dto.fecha);
        registro.setHorario(LocalTime.parse(dto.horario));

        // 1. Borrar alimentos previos del registro
        contieneRepo.deleteByRegistroId(registro.getId());

        // 2. Agregar los nuevos alimentos
        for (AlimentoCantidadDTO alimentoDTO : dto.alimentos) {
            Alimento alimento = alimentoRepo.findById(alimentoDTO.id)
                    .orElseThrow(() -> new RuntimeException("Alimento no encontrado"));

            Contiene contiene = new Contiene();
            ContieneId contieneId = new ContieneId();
            contieneId.setIdComida(registro.getId());
            contieneId.setIdAlimento(alimento.getId());

            contiene.setId(contieneId);
            contiene.setCantidad(alimentoDTO.cantidad);
            contiene.setRegistro(registro);
            contiene.setAlimento(alimento);

            contieneRepo.save(contiene);
        }

        return registroRepo.save(registro);
    }

}