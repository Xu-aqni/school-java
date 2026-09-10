package com.esercitazione.quizStoria.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.esercitazione.quizStoria.entities.Evento;
import com.esercitazione.quizStoria.services.EventoService;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/quiz")
public class EventoController {

    private final EventoService service;

    public EventoController(EventoService service) {
        this.service = service;
    }

    @GetMapping
    public String getStartingPage() {
        return "index";
    }

    @GetMapping("/events")
    public String getAllEvents(Model model) {
        List<Evento> events = service.getAllEvents();
        Long eventCount = service.getNumberOfEvents();

        model.addAttribute("events", events);
        model.addAttribute("eventCount", eventCount);
        return "eventi";
    }

    @GetMapping("/events/{id}")
    public String getEventById(@PathVariable Long id, Model model) {
        Evento event = service.getEventById(id);
        model.addAttribute("event", event);
        return "dettagliEvento";
    }

    @GetMapping("/events/oldest")
    public String getOldestEvent(Model model) {
        Evento event = service.getOldestEvent();
        model.addAttribute("event", event);
        return "dettagliEvento";
    }

    @GetMapping("/events/newest")
    public String getNewestEvent(Model model) {
        Evento event = service.getNewestEvent();
        model.addAttribute("event", event);
        return "dettagliEvento";
    }

    /*
     * @GetMapping("/events/total")
     * public Long getNumberOfEvents(Model model){
     * return service.getNumberOfEvents();
     * }
     */

    /*
     * @GetMapping("/events/total/{categoria}")
     * public Long getNumberOfEventsByCategory(@PathVariable String categoria){
     * return service.getNumberOfEventsByCategory(categoria);
     * }
     */

    @GetMapping("/events/search")
    public String searchEvent(
            @RequestParam(required = false) Integer anno,
            @RequestParam(required = false) String titolo,
            @RequestParam(required = false) String luogo,
            @RequestParam(required = false) String civilta,
            @RequestParam(required = false) String categoria,
            Model model) {

        List<Evento> events = service.searchEvents(anno, titolo, luogo, civilta, categoria);

        if (categoria != null && !categoria.isBlank()) {
            events = service.getEventByCategory(categoria);
        } else {
            events = service.getAllEvents();
        }

        model.addAttribute("events", events);
        model.addAttribute("eventCount", events.size());

        return "eventi";
    }

    @PostMapping("/events")
    public String createEvent(@ModelAttribute Evento event) {
        service.createEvent(event);
        return "redirect:/quiz/events";
    }

    @PostMapping("/events/{id}")
    public String updateEvent(@PathVariable Long id,
            @ModelAttribute Evento event) {
        service.updateEvent(id, event);
        return "redirect:/quiz/events";
    }

    @PostMapping("/events/{id}/delete")
    public String deleteEvent(@PathVariable Long id) {
        service.deleteEvent(id);
        return "redirect:/quiz/events";
    }
}
