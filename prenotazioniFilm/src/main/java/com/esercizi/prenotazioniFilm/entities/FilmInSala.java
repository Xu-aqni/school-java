package com.esercizi.prenotazioniFilm.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "filminsala")
public class FilmInSala {
    @Id @GeneratedValue
    private Long id;

    private LocalDate data;
    private double prezzo;
    private int postiRimanenti;

    @ManyToOne
    private Film film;

    @ManyToOne
    private Cinema cinema;

        // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public int getPostiRimanenti() {
        return postiRimanenti;
    }

    public void setPostiRimanenti(int postiRimanenti) {
        this.postiRimanenti = postiRimanenti;
    }
}
