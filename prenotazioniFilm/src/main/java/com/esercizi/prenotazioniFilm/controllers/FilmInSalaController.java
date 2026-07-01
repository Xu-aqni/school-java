package com.esercizi.prenotazioniFilm.controllers;

import com.esercizi.prenotazioniFilm.entities.FilmInSala;
import com.esercizi.prenotazioniFilm.services.FilmInSalaService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/programmazione")
public class FilmInSalaController {

    private FilmInSalaService fisService;

    public FilmInSalaController(FilmInSalaService fisService) {
        this.fisService = fisService;
    }

    //Azione Admin: Inserisce un film in una determinata sala/data (Form HTML)
    @PostMapping("/add")
    public String addProiezione(@ModelAttribute FilmInSala fis) {
        fisService.save(fis);
        return "redirect:/cinema/admin";
    }

    //Endpoint REST: Ritorna il palinsesto in JSON
    @GetMapping("/api")
    @ResponseBody
    public List<FilmInSala> getAllProgrammazioneRest() {
        return fisService.findAll();
    }
}