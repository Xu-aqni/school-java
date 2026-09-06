package com.esercitazione.quizStoria.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esercitazione.quizStoria.entities.Evento;
import com.esercitazione.quizStoria.repositories.EventoRepository;

import java.util.List;

@Service 
@Transactional(readOnly = true)
public class EventoService {
    private final EventoRepository repository;

    public EventoService(EventoRepository repository){
        this.repository = repository;
    }

    public List<Evento> getAllEvents(){
        return repository.findAll();
    }

    public Evento getEventById(Long id){
        return repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Evento con id: '" + id + "' non trovato"));
    }

    public List<Evento> getEventByYear(int anno){
        return repository.findByAnno(anno);
    }

    public List<Evento> getEventByTitle(String titolo){
        return repository.findByTitolo(titolo);
    }

    public List<Evento> getEventByCategory(String categoria){
        return repository.findByCategoria(categoria);
    }

    public List<Evento> getEventByCivilization(String civilta){
        return repository.findByCivilta(civilta);
    }

    public Evento getNewestEvent(){
        return repository.findFirstByOrderByAnnoDesc()
            .orElseThrow(() -> new IllegalStateException("Nessun evento trovato"));
    }

    public Evento getOldestEvent(){
        return repository.findFirstByOrderByAnnoAsc()
            .orElseThrow(() -> new IllegalStateException("Nessun evento trovato"));
    }

    public Long getNumberOfEvents(){
        return repository.count();
    }

    public Long getNumberOfEventsByCategory(String categoria){
        return repository.countByCategoria(categoria);
    }

    @Transactional 
    public Evento createEvent(Evento evento){
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
        return repository.save(evento);
    }

    @Transactional 
    public Evento updateEvent(Long id, Evento newEvent){
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
    public void deleteEvent(Long id){
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Evento con id: '" + id + "' non trovato");
        }

        repository.deleteById(id);
    }
}
