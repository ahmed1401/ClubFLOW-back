package com.clubflow.service;


import com.clubflow.dto.mapper.EventMapper;
import com.clubflow.dto.request.EventDto;
import com.clubflow.exception.ResourceNotFoundException;
import com.clubflow.model.Event;
import com.clubflow.model.User;
import com.clubflow.repository.EventRepository;
import com.clubflow.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventService {
    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;

    public EventService(EventRepository eventRepository,
                        UserRepository userRepository,
                        EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.eventMapper = eventMapper;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", id));
    }

    public List<Event> getEventsByClub(String club) {
        return eventRepository.findByClub(club);
    }

    public List<Event> getEventsByType(String type) {
        return eventRepository.findByType(type);
    }

    public List<Event> getEventsByDate(String date) {
        return eventRepository.findByDate(date);
    }

    public List<Event> searchEvents(String query) {
        return eventRepository.findByTitleContainingIgnoreCase(query);
    }

    @Transactional
    public Event createEvent(Long userId, EventDto eventDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Event event = eventMapper.toEntity(eventDto);
        event.setCreatedBy(user);

        Event savedEvent = eventRepository.save(event);
        logger.info("Event created successfully: {}", savedEvent.getTitle());
        return savedEvent;
    }

    @Transactional
    public Event updateEvent(Long id, EventDto eventDto) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", id));

        eventMapper.updateEntity(eventDto, event);
        Event updatedEvent = eventRepository.save(event);
        logger.info("Event updated successfully: {}", updatedEvent.getTitle());
        return updatedEvent;
    }

    @Transactional
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", id));
        eventRepository.delete(event);
        logger.info("Event deleted successfully: {}", id);
    }
}