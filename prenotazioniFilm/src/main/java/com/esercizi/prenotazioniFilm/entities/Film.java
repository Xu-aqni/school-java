package com.esercizi.prenotazioniFilm.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "film")
public class Film {
    @Id @GeneratedValue
    private Long id;

    private String titolo;
    private String regista;
    private String genere;

        // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getRegista() {
        return regista;
    }

    public void setRegista(String regista) {
        this.regista = regista;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }
}
