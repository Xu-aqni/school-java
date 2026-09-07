package com.esercitazione.quizStoria.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.esercitazione.quizStoria.entities.Evento;
import com.esercitazione.quizStoria.services.EventoService;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
@RequestMapping("/api/events")
public class EventoRestController {
    
    private final EventoService service;
    
    public EventoRestController(EventoService service){
        this.service = service;
    }

    @GetMapping
    public List<Evento> getAllEvents(){
        return service.getAllEvents();
    }
    
    @GetMapping("/{id}")
    public Evento getEventById(@PathVariable Long id){
        return service.getEventById(id);
    }

    @GetMapping("/oldest")
    public Evento getOldestEvent(){
        return service.getOldestEvent();
    }

    @GetMapping("/newest")
    public Evento getNewestEvent(){
        return service.getNewestEvent();
    }

    @GetMapping("/total")
    public Long getNumberOfEvents(){
        return service.getNumberOfEvents();
    }

    @GetMapping("/total/{categoria}")
    public Long getNumberOfEventsByCategory(@PathVariable String categoria){
        return service.getNumberOfEventsByCategory(categoria);
    }

    @GetMapping("/search")
    public List<Evento> searchEvent(
        @RequestParam(required = false) Integer anno,
        @RequestParam(required = false) String titolo,
        @RequestParam(required = false) String luogo,
        @RequestParam(required = false) String civilta,
        @RequestParam(required = false) String categoria){
        
        List<Evento> eventi;
        
        if(categoria != null && !categoria.isBlank()){
            eventi = service.getEventByCategory(categoria);
        } else {
            eventi = service.getAllEvents();
        }

        return eventi.stream()
            .filter(e -> titolo == null || titolo.isBlank() || 
                    e.getTitolo().toLowerCase().contains(titolo.toLowerCase()))
            .filter(e -> luogo == null || luogo.isBlank() || 
                    e.getLuogo().toLowerCase().contains(luogo.toLowerCase()))
            .filter(e -> civilta == null || civilta.isBlank() || 
                    e.getCivilta().toLowerCase().contains(civilta.toLowerCase()))
            .filter(e -> anno == null || e.getAnno().equals(anno))
                    .toList(); 
    }

    @PostMapping 
    public void createEvent(@RequestBody Evento event){
        service.createEvent(event);
    }

    @PutMapping ("/{id}")
    public void updateEvent(@PathVariable Long id,
                            @RequestBody Evento event){
        service.updateEvent(id, event);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id){
        service.deleteEvent(id);
    }
}
