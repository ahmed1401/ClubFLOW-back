package com.clubflow.repository;

import com.clubflow.model.MaterialRequest;
import com.clubflow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRequestRepository extends JpaRepository<MaterialRequest, Long> {
    List<MaterialRequest> findByClub(String club);
    List<MaterialRequest> findByRequestedBy(User user);
    List<MaterialRequest> findByStatus(MaterialRequest.RequestStatus status);
    List<MaterialRequest> findByRequestedByAndStatus(User user, MaterialRequest.RequestStatus status);
}