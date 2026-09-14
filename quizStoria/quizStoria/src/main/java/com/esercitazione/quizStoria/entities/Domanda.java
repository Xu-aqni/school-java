package com.esercitazione.quizStoria.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;

@Entity
public class Domanda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @Column(nullable = false, length = 300)
    private String contents;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id")
    private Evento event;

    public enum QuestionType {
        TRUEFALSE,
        GAPFILL,
        YEAR,
        PLACE,
        EVENT,
        CIVILIZATION,
        CATEGORY,
        ORDERING
    }

    @Transient
    public String getRispostaCorretta() {
        if (this.event == null || this.type == null) {
            return null;
        }

        return switch (this.type) {
            case YEAR -> String.valueOf(this.event.getAnno());
            case PLACE -> this.event.getLuogo();
            case CIVILIZATION -> this.event.getCivilta();
            case CATEGORY -> this.event.getCategoria();
            case EVENT -> this.event.getTitolo();
            default -> null; // Per TRUEFALSE, GAPFILL e ORDERING
        };
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Evento getEvent() {
        return event;
    }

    public void setEvent(Evento event) {
        this.event = event;
    }

}