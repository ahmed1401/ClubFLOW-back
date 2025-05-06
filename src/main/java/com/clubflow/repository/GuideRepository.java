package com.clubflow.repository;


import com.clubflow.model.Guide;
import com.clubflow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuideRepository extends JpaRepository<Guide, Long> {
    List<Guide> findByClub(String club);
    List<Guide> findByCreatedBy(User createdBy);

    @Query("SELECT g FROM Guide g WHERE LOWER(g.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(g.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Guide> searchGuides(@Param("query") String query);
}