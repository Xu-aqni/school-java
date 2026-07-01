package com.esercizi.prenotazioniFilm.controllers;

import com.esercizi.prenotazioniFilm.entities.Cinema;
import com.esercizi.prenotazioniFilm.services.CinemaService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cinema")
public class CinemaController {

    private final CinemaService cinemaService;

    public CinemaController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "admin";
    }

    //Endpoint REST: Ritorna la lista dei cinema in JSON
    @GetMapping("/api")
    @ResponseBody
    public List<Cinema> getAllCinemaRest() {
        return cinemaService.findAll();
    }
}