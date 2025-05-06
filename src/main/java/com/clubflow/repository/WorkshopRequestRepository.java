package com.clubflow.repository;

import com.clubflow.model.Member;
import com.clubflow.model.WorkshopRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkshopRequestRepository extends JpaRepository<WorkshopRequest, Long> {
    List<WorkshopRequest> findByMember(Member member);
    List<WorkshopRequest> findByStatus(WorkshopRequest.RequestStatus status);
    List<WorkshopRequest> findByMemberAndStatus(Member member, WorkshopRequest.RequestStatus status);
}