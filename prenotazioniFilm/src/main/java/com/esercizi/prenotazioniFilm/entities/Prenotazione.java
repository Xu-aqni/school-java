package com.esercizi.prenotazioniFilm.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Prenotazione {
    @Id @GeneratedValue
    private Long id;

    private String nome;
    private int postiPrenotati;

    @ManyToOne
    private FilmInSala filmInSala;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPostiPrenotati() {
        return postiPrenotati;
    }

    public void setPostiPrenotati(int postiPrenotati) {
        this.postiPrenotati = postiPrenotati;
    }

    public FilmInSala getFilmInSala() {
        return filmInSala;
    }

    public void setFilmInSala(FilmInSala filmInSala) {
        this.filmInSala = filmInSala;
    }
}
