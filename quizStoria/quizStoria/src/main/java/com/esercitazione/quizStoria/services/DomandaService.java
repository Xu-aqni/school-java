package com.esercitazione.quizStoria.services;

import com.esercitazione.quizStoria.dto.Difficulty;
import com.esercitazione.quizStoria.dto.DomandaConScelte;
import com.esercitazione.quizStoria.dto.RisultatoDomandaDTO;
import com.esercitazione.quizStoria.entities.Domanda;
import com.esercitazione.quizStoria.entities.Domanda.QuestionType;
import com.esercitazione.quizStoria.entities.Evento;
import com.esercitazione.quizStoria.repositories.DomandaRepository;
import com.esercitazione.quizStoria.repositories.EventoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DomandaService {

    private static final Random RANDOM = new Random();
    private static final int NUM_OPZIONI = 4;

    private final DomandaRepository domandaRepository;
    private final EventoRepository eventoRepository;

    public DomandaService(DomandaRepository domandaRepository, EventoRepository eventoRepository) {
        this.domandaRepository = domandaRepository;
        this.eventoRepository = eventoRepository;
    }

    @Transactional
    public List<Domanda> generateQuestionsForEvent(Evento evento) {
        List<Domanda> domandeGenerate = new ArrayList<>();

        if (evento.getAnno() != null) {
            Domanda dAnno = new Domanda();
            dAnno.setType(QuestionType.YEAR);
            dAnno.setContents("In quale anno si è verificato l'evento: '" + evento.getTitolo() + "'?");
            dAnno.setEvent(evento);
            domandeGenerate.add(dAnno);
        }

        if (StringUtils.hasText(evento.getLuogo())) {
            Domanda dLuogo = new Domanda();
            dLuogo.setType(QuestionType.PLACE);
            dLuogo.setContents("In quale luogo si è svolto l'evento: '" + evento.getTitolo() + "'?");
            dLuogo.setEvent(evento);
            domandeGenerate.add(dLuogo);
        }

        if (StringUtils.hasText(evento.getCivilta())) {
            Domanda dCivilta = new Domanda();
            dCivilta.setType(QuestionType.CIVILIZATION);
            dCivilta.setContents("A quale civiltà fa riferimento l'evento: '" + evento.getTitolo() + "'?");
            dCivilta.setEvent(evento);
            domandeGenerate.add(dCivilta);
        }

        if (StringUtils.hasText(evento.getCategoria())) {
            Domanda dCategoria = new Domanda();
            dCategoria.setType(QuestionType.CATEGORY);
            dCategoria.setContents("A quale categoria appartiene l'evento: '" + evento.getTitolo() + "'?");
            dCategoria.setEvent(evento);
            domandeGenerate.add(dCategoria);
        }

        evento.getDomande().addAll(domandeGenerate);
        return domandaRepository.saveAll(domandeGenerate);
    }

    public Domanda getDomandaById(Long id) {
        return domandaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Domanda con id: '" + id + "' non trovata"));
    }

    public List<Domanda> getDomandeByEventoId(Long eventoId) {
        return domandaRepository.findByEventId(eventoId);
    }

    public List<Domanda> getAllDomande() {
        return domandaRepository.findAllByOrderByIdAsc();
    }

    public List<Domanda> searchDomande(QuestionType type, String contents, String eventoTitolo) {
        String checkedContents = StringUtils.hasText(contents) ? contents.trim() : null;
        String checkedTitolo = StringUtils.hasText(eventoTitolo) ? eventoTitolo.trim() : null;
        return domandaRepository.searchDomande(type, checkedContents, checkedTitolo);
    }

    public boolean checkAnswer(Long domandaId, String userAnswer) {
        if (!StringUtils.hasText(userAnswer)) {
            return false;
        }

        Domanda domanda = getDomandaById(domandaId);
        String rispostaCorretta = domanda.getRispostaCorretta();

        if (rispostaCorretta == null) {
            return false;
        }

        return rispostaCorretta.trim().equalsIgnoreCase(userAnswer.trim());
    }


    public List<DomandaConScelte> getRandomQuiz(int limit, Difficulty difficulty) {
        List<Domanda> domande = domandaRepository.findRandomQuestions(limit);
        List<Evento> tuttiGliEventi = eventoRepository.findAll();

        List<DomandaConScelte> quiz = new ArrayList<>();
        for (Domanda domanda : domande) {
            quiz.add(costruisciScelte(domanda, difficulty, tuttiGliEventi));
        }
        return quiz;
    }

    public List<RisultatoDomandaDTO> valutaSessione(List<Long> domandaIds, Map<Long, String> risposteUtente) {
        List<RisultatoDomandaDTO> risultati = new ArrayList<>();

        for (Long id : domandaIds) {
            Domanda domanda = getDomandaById(id);
            String rispostaUtente = risposteUtente.get(id);
            boolean corretta = checkAnswer(id, rispostaUtente);

            risultati.add(new RisultatoDomandaDTO(
                    domanda.getContents(),
                    rispostaUtente,
                    domanda.getRispostaCorretta(),
                    corretta));
        }
        return risultati;
    }

    private DomandaConScelte costruisciScelte(Domanda domanda, Difficulty difficulty, List<Evento> tuttiGliEventi) {
        String corretta = domanda.getRispostaCorretta();
        List<String> opzioni = new ArrayList<>();

        if (corretta != null && domanda.getEvent() != null) {
            opzioni.add(corretta);
            opzioni.addAll(generaDistrattori(domanda, difficulty, tuttiGliEventi));
        }

        Collections.shuffle(opzioni);
        return new DomandaConScelte(domanda.getId(), domanda.getType(), domanda.getContents(), opzioni);
    }

    private Set<String> generaDistrattori(Domanda domanda, Difficulty difficulty, List<Evento> tuttiGliEventi) {
        Evento eventoCorretto = domanda.getEvent();
        String corretta = domanda.getRispostaCorretta();

        List<Evento> altriEventi = tuttiGliEventi.stream()
                .filter(e -> !Objects.equals(e.getId(), eventoCorretto.getId()))
                .collect(Collectors.toList());

        return switch (domanda.getType()) {
            case YEAR -> generaDistrattoriAnno(eventoCorretto.getAnno(), altriEventi, difficulty);
            case PLACE -> generaDistrattoriCampo(altriEventi, difficulty, eventoCorretto, corretta, Evento::getLuogo);
            case CIVILIZATION -> generaDistrattoriCampo(altriEventi, difficulty, eventoCorretto, corretta, Evento::getCivilta);
            case CATEGORY -> generaDistrattoriCampo(altriEventi, difficulty, eventoCorretto, corretta, Evento::getCategoria);
            case EVENT -> generaDistrattoriCampo(altriEventi, difficulty, eventoCorretto, corretta, Evento::getTitolo);
            default -> Set.of();
        };
    }

    private Set<String> generaDistrattoriAnno(Integer annoCorretto, List<Evento> altriEventi, Difficulty difficulty) {
        if (annoCorretto == null) {
            return Set.of();
        }

        int minGap;
        int maxGap;
        switch (difficulty) {
            case DIFFICILE -> { minGap = 1; maxGap = 15; }
            case MEDIO -> { minGap = 15; maxGap = 100; }
            default -> { minGap = 100; maxGap = 600; }
        }

        Set<Integer> usati = new LinkedHashSet<>();
        usati.add(annoCorretto);
        Set<Integer> scelti = new LinkedHashSet<>();

        List<Integer> anniCandidati = altriEventi.stream()
                .map(Evento::getAnno)
                .filter(Objects::nonNull)
                .filter(anno -> {
                    int gap = Math.abs(anno - annoCorretto);
                    return gap >= minGap && gap <= maxGap;
                })
                .distinct()
                .collect(Collectors.toList());
        Collections.shuffle(anniCandidati);

        for (Integer anno : anniCandidati) {
            if (scelti.size() >= NUM_OPZIONI - 1) break;
            if (usati.add(anno)) scelti.add(anno);
        }

        int tentativi = 0;
        while (scelti.size() < NUM_OPZIONI - 1 && tentativi < 50) {
            tentativi++;
            int offset = minGap + RANDOM.nextInt(Math.max(1, maxGap - minGap + 1));
            int segno = RANDOM.nextBoolean() ? 1 : -1;
            int anno = annoCorretto + (segno * offset);
            if (usati.add(anno)) scelti.add(anno);
        }

        return scelti.stream().map(String::valueOf).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Set<String> generaDistrattoriCampo(List<Evento> altriEventi, Difficulty difficulty,
            Evento eventoCorretto, String corretta, Function<Evento, String> estrattore) {

        Set<String> usati = new LinkedHashSet<>();
        if (corretta != null) usati.add(corretta);
        Set<String> risultato = new LinkedHashSet<>();

        List<Evento> candidatiPerDifficolta = new ArrayList<>(
                filtraPerDifficolta(altriEventi, eventoCorretto, difficulty));
        Collections.shuffle(candidatiPerDifficolta);
        riempiDaCandidati(candidatiPerDifficolta, estrattore, usati, risultato, NUM_OPZIONI - 1);

        if (risultato.size() < NUM_OPZIONI - 1) {
            List<Evento> tutti = new ArrayList<>(altriEventi);
            Collections.shuffle(tutti);
            riempiDaCandidati(tutti, estrattore, usati, risultato, NUM_OPZIONI - 1);
        }

        int i = 1;
        while (risultato.size() < NUM_OPZIONI - 1) {
            String placeholder = "Opzione " + i++;
            if (usati.add(placeholder)) risultato.add(placeholder);
        }

        return risultato;
    }

    private void riempiDaCandidati(List<Evento> candidati, Function<Evento, String> estrattore,
            Set<String> usati, Set<String> risultato, int target) {

        for (Evento e : candidati) {
            if (risultato.size() >= target) break;
            String valore = estrattore.apply(e);
            if (valore != null && usati.add(valore)) {
                risultato.add(valore);
            }
        }
    }

    private List<Evento> filtraPerDifficolta(List<Evento> altriEventi, Evento eventoCorretto, Difficulty difficulty) {
        Integer annoCorretto = eventoCorretto.getAnno();
        String civiltaCorretta = eventoCorretto.getCivilta();
        String categoriaCorretta = eventoCorretto.getCategoria();

        return switch (difficulty) {
            case DIFFICILE -> altriEventi.stream()
                    .filter(e -> Objects.equals(e.getCivilta(), civiltaCorretta))
                    .filter(e -> annoCorretto == null || e.getAnno() == null
                            || Math.abs(e.getAnno() - annoCorretto) <= 100)
                    .collect(Collectors.toList());
            case MEDIO -> altriEventi.stream()
                    .filter(e -> Objects.equals(e.getCategoria(), categoriaCorretta)
                            || (annoCorretto != null && e.getAnno() != null
                                    && Math.abs(e.getAnno() - annoCorretto) <= 300))
                    .collect(Collectors.toList());
            default -> altriEventi.stream() // FACILE
                    .filter(e -> !Objects.equals(e.getCivilta(), civiltaCorretta))
                    .collect(Collectors.toList());
        };
    }
}
