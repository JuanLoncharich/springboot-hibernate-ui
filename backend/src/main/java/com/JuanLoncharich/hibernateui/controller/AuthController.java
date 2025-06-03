package com.JuanLoncharich.hibernateui.controller;

import com.JuanLoncharich.hibernateui.model.User;
import com.JuanLoncharich.hibernateui.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:8080")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
        }

        User user = userOpt.get();
        if (!user.getContraseña().equals(request.password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta");
        }

        return ResponseEntity.ok(user);
    }

    // REGISTER
    @PostMapping("/register")
    public User register(@Valid @RequestBody RegisterRequest request) {
        Optional<User> existing = userRepository.findByEmail(request.email);
        if (existing.isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }

        User newUser = new User();
        newUser.setNombre(request.nombre);
        newUser.setEmail(request.email);
        newUser.setContraseña(request.password);
        newUser.setFechaNacimiento(request.fechaNacimiento);
        newUser.setGenero(request.genero);
        newUser.setAltura(request.altura);
        newUser.setPesoActual(request.pesoActual);

        return userRepository.save(newUser);
    }

    // Helper classes for requests

    static class LoginRequest {
        public String email;
        public String password;
    }

    static class RegisterRequest {

        @NotBlank(message = "Nombre es obligatorio")
        public String nombre;

        @Email(message = "Correo electrónico inválido")
        public String email;

        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
        public String password;

        @NotNull(message = "Fecha de nacimiento es obligatoria")
        public LocalDate fechaNacimiento;

        @NotBlank(message = "Género es obligatorio")
        public String genero;

        @NotNull(message = "Altura es obligatoria")
        @DecimalMin(value = "0.1", message = "La altura debe ser mayor a 0")
        public Double altura;

        @NotNull(message = "Peso actual es obligatorio")
        @DecimalMin(value = "0.1", message = "El peso debe ser mayor a 0")
        public Double pesoActual;
    }
}