package co.edu.umanizales.tallerautomotriz_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/taller")

public class TallerController {
    @GetMapping
    public String getHello(){
        return "Hola Campeones y campeonas";
    }
}