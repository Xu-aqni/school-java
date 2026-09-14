package com.esercitazione.quizStoria.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.esercitazione.quizStoria.entities.Domanda;
import com.esercitazione.quizStoria.entities.Evento;
import com.esercitazione.quizStoria.repositories.EventoRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class EventoService {
    private final EventoRepository repository;
    private final DomandaService domandaService;

    public EventoService(EventoRepository repository, DomandaService domandaService) {
        this.repository = repository;
        this.domandaService = domandaService;
    }

    public List<Evento> getAllEvents() {
        return repository.findAllByOrderByIdAsc();
    }

    public Evento getEventById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento con id: '" + id + "' non trovato"));
    }

    public List<Evento> getEventByYear(Integer anno) {
        return repository.findByAnno(anno);
    }

    public List<Evento> getEventByTitle(String titolo) {
        return repository.findByTitolo(titolo);
    }

    public List<Evento> getEventByCategory(String categoria) {
        return repository.findByCategoria(categoria);
    }

    public List<Evento> getEventByCivilization(String civilta) {
        return repository.findByCivilta(civilta);
    }

    public Evento getNewestEvent() {
        return repository.findFirstByOrderByAnnoDesc()
                .orElseThrow(() -> new IllegalStateException("Nessun evento trovato"));
    }

    public Evento getOldestEvent() {
        return repository.findFirstByOrderByAnnoAsc()
                .orElseThrow(() -> new IllegalStateException("Nessun evento trovato"));
    }

    public Long getNumberOfEvents() {
        return repository.count();
    }

    public List<Evento> searchEvents(Integer anno, String titolo, String luogo, String civilta, String categoria) {

        String checkedTitle = StringUtils.hasText(titolo) ? titolo.trim() : null;
        String checkedPlace = StringUtils.hasText(luogo) ? luogo.trim() : null;
        String checkedCivilization = StringUtils.hasText(civilta) ? civilta.trim() : null;
        String checkedCategory = StringUtils.hasText(categoria) ? categoria.trim() : null;

        return repository.searchEvents(anno, checkedTitle, checkedPlace, checkedCivilization, checkedCategory);
    }

    @Transactional
    public Evento createEvent(Evento evento) {
        if (evento.getAnno() == null) {
            throw new IllegalArgumentException("L'evento deve avere un anno");
        }
        if (evento.getTitolo() == null || evento.getTitolo().trim().isEmpty()) {
            throw new IllegalArgumentException("L'evento deve avere un titolo");
        }
        if (evento.getLuogo() == null || evento.getLuogo().trim().isEmpty()) {
            throw new IllegalArgumentException("L'evento deve avere un luogo");
        }
        if (evento.getCivilta() == null || evento.getCivilta().trim().isEmpty()) {
            throw new IllegalArgumentException("L'evento deve avere una civilta");
        }
        if (evento.getCategoria() == null || evento.getCategoria().trim().isEmpty()) {
            throw new IllegalArgumentException("L'evento deve avere una categoria");
        }

        Evento savedEvent = repository.save(evento);

        domandaService.generateQuestionsForEvent(savedEvent);

        return savedEvent;
    }

    @Transactional
    public int refreshAllQuestions() {
        List<Evento> allEvents = repository.findAll();
        int generated = 0;

        for (Evento evento : allEvents) {
            List<Domanda> esistenti = domandaService.getDomandeByEventoId(evento.getId());
            if (esistenti.isEmpty()) {
                domandaService.generateQuestionsForEvent(evento);
                generated++;
            }
        }
        return generated;
    }

    @Transactional
    public Evento updateEvent(Long id, Evento newEvent) {
        Evento updatingEvent = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento con id: '" + id + "' non trovato"));

        if (newEvent.getAnno() == null) {
            throw new IllegalArgumentException("L'evento deve avere un anno");
        }
        if (newEvent.getTitolo() == null || newEvent.getTitolo().trim().isEmpty()) {
            throw new IllegalArgumentException("L'evento deve avere un titolo");
        }
        if (newEvent.getLuogo() == null || newEvent.getLuogo().trim().isEmpty()) {
            throw new IllegalArgumentException("L'evento deve avere un luogo");
        }
        if (newEvent.getCivilta() == null || newEvent.getCivilta().trim().isEmpty()) {
            throw new IllegalArgumentException("L'evento deve avere una civilta");
        }
        if (newEvent.getCategoria() == null || newEvent.getCategoria().trim().isEmpty()) {
            throw new IllegalArgumentException("L'evento deve avere una categoria");
        }

        updatingEvent.setAnno(newEvent.getAnno());
        updatingEvent.setTitolo(newEvent.getTitolo());
        updatingEvent.setLuogo(newEvent.getLuogo());
        updatingEvent.setCivilta(newEvent.getCivilta());
        updatingEvent.setCategoria(newEvent.getCategoria());
        updatingEvent.setDescrizione(newEvent.getDescrizione());

        return repository.save(updatingEvent);
    }

    @Transactional
    public void deleteEvent(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Evento con id: '" + id + "' non trovato");
        }

        repository.deleteById(id);
    }
}
