package com.clubflow.repository;


import com.clubflow.model.Event;
import com.clubflow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByClub(String club);
    List<Event> findByType(String type);
    List<Event> findByDate(String date);
    List<Event> findByCreatedBy(User createdBy);
    List<Event> findByTitleContainingIgnoreCase(String title);
}