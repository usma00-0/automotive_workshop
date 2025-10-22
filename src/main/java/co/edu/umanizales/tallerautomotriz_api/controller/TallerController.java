package co.edu.umanizales.tallerautomotriz_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TallerController {
    
    @GetMapping
    public Map<String, String> getInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("nombre", "Sistema de Gestión de Taller Automotriz");
        info.put("version", "1.0.0");
        info.put("descripcion", "API REST para gestión de taller automotriz con persistencia en CSV");
        return info;
    }
    
    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("message", "Taller Automotriz API is running");
        return status;
    }
}
