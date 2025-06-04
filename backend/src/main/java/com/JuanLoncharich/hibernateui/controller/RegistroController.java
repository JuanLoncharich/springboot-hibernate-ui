package com.JuanLoncharich.hibernateui.controller;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.JuanLoncharich.hibernateui.dto.ComidaManualRequest;
import com.JuanLoncharich.hibernateui.dto.RegistroAlimentoDTO;
import com.JuanLoncharich.hibernateui.model.Alimento;
import com.JuanLoncharich.hibernateui.model.Contiene;
import com.JuanLoncharich.hibernateui.model.ContieneId;
import com.JuanLoncharich.hibernateui.model.Registro;
import com.JuanLoncharich.hibernateui.repository.AlimentoRepository;
import com.JuanLoncharich.hibernateui.repository.ContieneRepository;
import com.JuanLoncharich.hibernateui.repository.RegistroRepository;
import com.JuanLoncharich.hibernateui.service.RegistroService;

@RestController
@RequestMapping("/api/registros")
public class RegistroController {

    private final RegistroService registroService;
    private final RegistroRepository registroRepo;
    private final ContieneRepository contieneRepo;

    @Autowired
    private AlimentoRepository alimentoRepository;


    public RegistroController(RegistroService registroService,
                              RegistroRepository registroRepo,
                              ContieneRepository contieneRepo) {
        this.registroService = registroService;
        this.registroRepo = registroRepo;
        this.contieneRepo = contieneRepo;
    }

    @GetMapping("/alimentos")
    public ResponseEntity<List<RegistroAlimentoDTO>> getRegistrosConAlimentos(@RequestParam Long idUsuario) {
        //System.out.println("Llega idUsuario: " + idUsuario);
        List<RegistroAlimentoDTO> registros = registroService.getRegistrosConAlimentos(idUsuario);
        //System.out.println("Cantidad de registros: " + registros.size());
        return ResponseEntity.ok(registros);
    }


    @PostMapping("/registrar-comida-manual")
    public ResponseEntity<?> crearRegistroConComidaManual(@RequestBody ComidaManualRequest request) {
        if (request.cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }

        // 1. Crear un nuevo Alimento para esta comida manual
        Alimento alimentoManual = new Alimento();
        alimentoManual.setNombre(request.nombre);
        alimentoManual.setCalorias(request.calorias);
        alimentoManual.setProteinas(request.proteinas);
        alimentoManual.setCarbohidratos(request.carbohidratos);
        alimentoManual.setGrasas(request.grasas);
        alimentoManual.setPorcion(request.cantidad);


        Alimento savedAlimento = alimentoRepository.save(alimentoManual);

        // 2. Crear el Registro
        Registro registro = new Registro();
        registro.setFecha(request.fecha);
        registro.setHorario(LocalTime.parse(request.horario));
        registro.setIdUsuario(request.idUsuario);

        Registro savedRegistro = registroRepo.save(registro);

        // 3. Crear Contiene con el alimento recién creado
        Contiene contiene = new Contiene();

        ContieneId id = new ContieneId();
        id.setIdComida(savedRegistro.getId());
        id.setIdAlimento(savedAlimento.getId());

        contiene.setId(id);
        contiene.setCantidad(request.cantidad);
        contiene.setRegistro(savedRegistro);
        contiene.setAlimento(savedAlimento);

        contieneRepo.save(contiene);

        return ResponseEntity.ok("✅ Registro creado con comida personalizada");
    }


}