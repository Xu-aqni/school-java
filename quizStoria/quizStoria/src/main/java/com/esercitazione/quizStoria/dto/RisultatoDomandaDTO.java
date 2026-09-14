package com.esercitazione.quizStoria.dto;

public class RisultatoDomandaDTO {

    private final String contents;
    private final String userAnswer;
    private final String correctAnswer;
    private final boolean correct;

    public RisultatoDomandaDTO(String contents, String userAnswer, String correctAnswer, boolean correct) {
        this.contents = contents;
        this.userAnswer = userAnswer;
        this.correctAnswer = correctAnswer;
        this.correct = correct;
    }

    public String getContents() {
        return contents;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public boolean isCorrect() {
        return correct;
    }
}
