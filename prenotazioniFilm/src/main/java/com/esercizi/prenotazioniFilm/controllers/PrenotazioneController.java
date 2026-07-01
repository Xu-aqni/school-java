package com.esercizi.prenotazioniFilm.controllers;

import com.esercizi.prenotazioniFilm.entities.Prenotazione;
import com.esercizi.prenotazioniFilm.services.PrenotazioneService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/prenotazioni")
public class PrenotazioneController {

    private PrenotazioneService prenoService;

    public PrenotazioneController(PrenotazioneService prenoService) {
        this.prenoService = prenoService;
    }

    //Ricezione dati dal form di prenotazione (HTML)
    @PostMapping("/effettua")
    public String prenota(@RequestParam Long idFilmInSala, 
                          @RequestParam String nome, 
                          @RequestParam Integer posti) {
        
        boolean successo = prenoService.effettuaPrenotazione(idFilmInSala, nome, posti);
        return successo ? "redirect:/film" : "redirect:/cinema/errore";
    }

    //Endpoint REST: Ritorna tutte le prenotazioni effettuate in JSON
    @GetMapping("/api")
    @ResponseBody
    public List<Prenotazione> getAllPrenotazioniRest() {
        return prenoService.findAll();
    }
}
