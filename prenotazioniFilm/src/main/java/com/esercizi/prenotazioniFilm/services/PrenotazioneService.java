package com.esercizi.prenotazioniFilm.services;

import com.esercizi.prenotazioniFilm.entities.*;
import com.esercizi.prenotazioniFilm.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PrenotazioneService {

    private final PrenotazioneRepository prenotazioneRepository;
    private final FilmInSalaRepository filmInSalaRepository;

    public PrenotazioneService(PrenotazioneRepository prenotazioneRepository, FilmInSalaRepository filmInSalaRepository) {
        this.prenotazioneRepository = prenotazioneRepository;
        this.filmInSalaRepository = filmInSalaRepository;
    }

    public List<Prenotazione> findAll() {
        return prenotazioneRepository.findAll();
    }

    @Transactional
    public boolean effettuaPrenotazione(Long idFilmInSala, String nome, Integer posti) {
        FilmInSala fis = filmInSalaRepository.findById(idFilmInSala).orElse(null);
        if (fis != null && fis.getPostiRimanenti() >= posti) {
            fis.setPostiRimanenti(fis.getPostiRimanenti() - posti);
            filmInSalaRepository.save(fis);

            Prenotazione p = new Prenotazione();
            p.setFilmInSala(fis);
            p.setNome(nome);
            p.setPostiPrenotati(posti);
            prenotazioneRepository.save(p);
            return true;
        }
        return false;
    }
}
