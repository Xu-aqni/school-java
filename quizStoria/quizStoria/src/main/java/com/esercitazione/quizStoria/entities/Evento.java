package com.esercitazione.quizStoria.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer anno;

    @Column(nullable = false, length = 200)
    private String titolo;

    @Column(nullable = false, length = 100)
    private String luogo;

    @Column(nullable = false, length = 100)
    private String civilta;

    @Column(nullable = false, length = 50)
    private String categoria;

    @Column(columnDefinition = "TEXT")
    private String descrizione;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Domanda> domande = new ArrayList<>();

    // Getters e setters

    public Long getId() {
        return id;
    }

    public Integer getAnno() {
        return anno;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getLuogo() {
        return luogo;
    }

    public String getCivilta() {
        return civilta;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public void setCivilta(String civilta) {
        this.civilta = civilta;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public List<Domanda> getDomande() {
        return domande;
    }

    public void setDomande(List<Domanda> domande) {
        this.domande = domande;
    }
}
