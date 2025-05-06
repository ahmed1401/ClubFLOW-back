package com.clubflow.repository;


import com.clubflow.model.BusRequest;
import com.clubflow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusRequestRepository extends JpaRepository<BusRequest, Long> {
    List<BusRequest> findByUser(User user);
    List<BusRequest> findByStatus(BusRequest.RequestStatus status);
    List<BusRequest> findByUserAndStatus(User user, BusRequest.RequestStatus status);
}