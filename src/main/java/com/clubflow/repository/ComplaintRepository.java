package com.clubflow.repository;

import com.clubflow.model.Complaint;
import com.clubflow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByClub(String club);
    List<Complaint> findBySubject(String subject);
    List<Complaint> findBySubmittedBy(User user);
    List<Complaint> findByStatus(Complaint.ComplaintStatus status);
    List<Complaint> findBySubmittedByAndStatus(User user, Complaint.ComplaintStatus status);
}