package com.esercizi.prenotazioniFilm.services;

import com.esercizi.prenotazioniFilm.entities.Cinema;
import com.esercizi.prenotazioniFilm.repositories.CinemaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CinemaService {
    private final CinemaRepository cinemaRepository;

    public CinemaService(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    public List<Cinema> findAll() { return cinemaRepository.findAll(); }
}
