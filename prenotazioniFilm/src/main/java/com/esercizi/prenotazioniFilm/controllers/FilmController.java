package com.esercizi.prenotazioniFilm.controllers;

import com.esercizi.prenotazioniFilm.entities.Film;
import com.esercizi.prenotazioniFilm.services.FilmInSalaService;
import com.esercizi.prenotazioniFilm.services.FilmService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/film")
public class FilmController {

    private FilmService filmService;
    private FilmInSalaService fisService;

    public FilmController(FilmService filmService, FilmInSalaService fisService) {
        this.filmService = filmService;
        this.fisService = fisService;
    }

    //Vista catalogo film (HTML)
    @GetMapping
    public String elencoFilm(Model model) {
        model.addAttribute("films", filmService.findAll());
        return "elenco-film";
    }

    //Vista dettaglio del film selezionato e relative proiezioni (HTML)
    @GetMapping("/{id}")
    public String dettaglioFilm(@PathVariable Long id, Model model) {
        model.addAttribute("film", filmService.findById(id));
        model.addAttribute("proiezioni", fisService.findByFilm(id)); // Recupera i cinema e date disponibili
        return "dettaglio-film";
    }

    //Azione Admin: Carica un nuovo film (Form HTML)
    @PostMapping("/add")
    public String addFilm(@ModelAttribute Film film) {
        filmService.save(film);
        return "redirect:/cinema/admin"; // Ritorna al pannello admin centrale
    }

    //Endpoint REST: Ritorna la lista dei film in JSON
    @GetMapping("/api")
    @ResponseBody
    public List<Film> getAllFilmRest() {
        return filmService.findAll();
    }
}
