package com.clubflow.repository;

import com.clubflow.model.RoomRequest;
import com.clubflow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRequestRepository extends JpaRepository<RoomRequest, Long> {
    List<RoomRequest> findByUser(User user);

    List<RoomRequest> findByStatus(RoomRequest.RequestStatus status);

    List<RoomRequest> findByUserAndStatus(User user, RoomRequest.RequestStatus status);

    @Query(value = "SELECT * FROM room_requests r WHERE r.room_number = :roomNumber " +
            "AND r.date = :date " +
            "AND ((TIME(r.start_time) <= TIME(:endTime) AND " +
            "ADDTIME(TIME(r.start_time), SEC_TO_TIME(r.duration * 60)) >= TIME(:startTime)) OR " +
            "(TIME(r.start_time) >= TIME(:startTime) AND TIME(r.start_time) <= TIME(:endTime))) " +
            "AND r.status != 'REJECTED'", nativeQuery = true)
    List<RoomRequest> findOverlappingRequests(
            @Param("roomNumber") String roomNumber,
            @Param("date") String date,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime
    );
}