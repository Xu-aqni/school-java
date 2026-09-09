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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;



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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Evento event;

    //@ManyToOne 
    //risposta della domanda

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
}
