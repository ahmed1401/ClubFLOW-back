package com.clubflow.controller;


import com.clubflow.dto.request.EventDto;
import com.clubflow.dto.response.ApiResponse;
import com.clubflow.model.Event;
import com.clubflow.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Events", description = "Event management API")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    @Operation(summary = "Get all events")
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get event by ID")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Event event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/club/{club}")
    @Operation(summary = "Get events by club")
    public ResponseEntity<List<Event>> getEventsByClub(@PathVariable String club) {
        List<Event> events = eventService.getEventsByClub(club);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get events by type")
    public ResponseEntity<List<Event>> getEventsByType(@PathVariable String type) {
        List<Event> events = eventService.getEventsByType(type);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "Get events by date")
    public ResponseEntity<List<Event>> getEventsByDate(@PathVariable String date) {
        List<Event> events = eventService.getEventsByDate(date);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/search")
    @Operation(summary = "Search events by title")
    public ResponseEntity<List<Event>> searchEvents(@RequestParam String query) {
        List<Event> events = eventService.searchEvents(query);
        return ResponseEntity.ok(events);
    }

    @PostMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new event (Admin only)")
    public ResponseEntity<ApiResponse> createEvent(
            @PathVariable Long userId,
            @Valid @RequestBody EventDto eventDto) {
        Event event = eventService.createEvent(userId, eventDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Event created successfully", event));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update event (Admin only)")
    public ResponseEntity<ApiResponse> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody EventDto eventDto) {
        Event event = eventService.updateEvent(id, eventDto);
        return ResponseEntity.ok(new ApiResponse(true, "Event updated successfully", event));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete event (Admin only)")
    public ResponseEntity<ApiResponse> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok(new ApiResponse(true, "Event deleted successfully"));
    }
}