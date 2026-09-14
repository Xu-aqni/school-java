package com.esercitazione.quizStoria.dto;

import com.esercitazione.quizStoria.entities.Domanda.QuestionType;

import java.util.List;

public class DomandaConScelte {

    private final Long id;
    private final QuestionType type;
    private final String contents;
    private final List<String> opzioni;

    public DomandaConScelte(Long id, QuestionType type, String contents, List<String> opzioni) {
        this.id = id;
        this.type = type;
        this.contents = contents;
        this.opzioni = opzioni;
    }

    public Long getId() {
        return id;
    }

    public QuestionType getType() {
        return type;
    }

    public String getContents() {
        return contents;
    }

    public List<String> getOpzioni() {
        return opzioni;
    }
}
