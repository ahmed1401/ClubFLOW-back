package com.clubflow;

import com.clubflow.dto.mapper.EventMapper;
import com.clubflow.dto.request.EventDto;
import com.clubflow.exception.ResourceNotFoundException;
import com.clubflow.model.Event;
import com.clubflow.model.User;
import com.clubflow.repository.EventRepository;
import com.clubflow.repository.UserRepository;
import com.clubflow.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEvents() {
        List<Event> mockEvents = List.of(new Event(), new Event());
        when(eventRepository.findAll()).thenReturn(mockEvents);

        List<Event> result = eventService.getAllEvents();

        assertEquals(2, result.size());
        verify(eventRepository, times(1)).findAll();
    }

    @Test
    void testGetEventById_Valid() {
        Event event = new Event();
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Event result = eventService.getEventById(1L);

        assertEquals(event, result);
        verify(eventRepository).findById(1L);
    }

    @Test
    void testGetEventById_NotFound() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> eventService.getEventById(1L));
    }

    @Test
    void testGetEventsByClub() {
        List<Event> events = List.of(new Event(), new Event());
        when(eventRepository.findByClub("Tech Club")).thenReturn(events);

        List<Event> result = eventService.getEventsByClub("Tech Club");

        assertEquals(2, result.size());
    }

    @Test
    void testCreateEvent() {
        User user = new User();
        EventDto dto = new EventDto("Title", "Desc", "Workshop", "Tech Club", "2025-05-10", "18:00", "Room A");
        Event mappedEvent = new Event();
        Event savedEvent = new Event();
        savedEvent.setTitle("Title");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(eventMapper.toEntity(dto)).thenReturn(mappedEvent);
        when(eventRepository.save(mappedEvent)).thenReturn(savedEvent);

        Event result = eventService.createEvent(1L, dto);

        assertEquals("Title", result.getTitle());
        verify(userRepository).findById(1L);
        verify(eventRepository).save(mappedEvent);
    }


    @Test
    void testDeleteEvent() {
        Event event = new Event();
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        eventService.deleteEvent(1L);

        verify(eventRepository).delete(event);
    }

    @Test
    void testDeleteEvent_NotFound() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> eventService.deleteEvent(1L));
    }
}
