package com.esercitazione.quizStoria.controllers;

import com.esercitazione.quizStoria.dto.Difficulty;
import com.esercitazione.quizStoria.dto.DomandaConScelte;
import com.esercitazione.quizStoria.dto.RisultatoDomandaDTO;
import com.esercitazione.quizStoria.entities.Domanda;
import com.esercitazione.quizStoria.entities.Domanda.QuestionType;
import com.esercitazione.quizStoria.services.DomandaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/quiz/questions")
public class DomandaController {

    private final DomandaService domandaService;

    public DomandaController(DomandaService domandaService) {
        this.domandaService = domandaService;
    }

    @GetMapping("/play")
    public String startQuiz(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "MEDIO") Difficulty difficulty,
            Model model) {

        List<DomandaConScelte> questions = domandaService.getRandomQuiz(limit, difficulty);

        model.addAttribute("questions", questions);
        model.addAttribute("limit", limit);
        model.addAttribute("difficulty", difficulty);
        model.addAttribute("difficulties", Difficulty.values());
        return "playQuiz";
    }

    @PostMapping("/submit")
    public String submitQuiz(
            @RequestParam List<Long> questionIds,
            @RequestParam Map<String, String> allParams,
            Model model) {

        Map<Long, String> risposteUtente = new HashMap<>();
        for (Long id : questionIds) {
            risposteUtente.put(id, allParams.get("risposta_" + id));
        }

        List<RisultatoDomandaDTO> risultati = domandaService.valutaSessione(questionIds, risposteUtente);

        long corrette = risultati.stream().filter(RisultatoDomandaDTO::isCorrect).count();
        long totale = risultati.size();
        long errate = totale - corrette;
        double percentuale = totale == 0 ? 0 : (corrette * 100.0 / totale);

        model.addAttribute("risultati", risultati);
        model.addAttribute("corrette", corrette);
        model.addAttribute("errate", errate);
        model.addAttribute("totale", totale);
        model.addAttribute("percentuale", percentuale);
        return "risultatoQuiz";
    }

    @GetMapping
    public String getAllQuestions(Model model) {
        List<Domanda> questions = domandaService.getAllDomande();

        model.addAttribute("questions", questions);
        model.addAttribute("questionCount", questions.size());
        model.addAttribute("types", QuestionType.values());
        return "domande";
    }

    @GetMapping("/search")
    public String searchQuestions(
            @RequestParam(required = false) QuestionType type,
            @RequestParam(required = false) String contents,
            @RequestParam(required = false) String eventoTitolo,
            Model model) {

        List<Domanda> questions = domandaService.searchDomande(type, contents, eventoTitolo);

        model.addAttribute("questions", questions);
        model.addAttribute("questionCount", questions.size());
        model.addAttribute("types", QuestionType.values());
        return "domande";
    }

    @GetMapping("/event/{eventoId}")
    public String getQuestionsByEvent(@PathVariable Long eventoId, Model model) {
        List<Domanda> questions = domandaService.getDomandeByEventoId(eventoId);
        model.addAttribute("questions", questions);
        return "domandeEvento";
    }
}
