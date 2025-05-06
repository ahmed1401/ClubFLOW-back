package com.clubflow.controller;


import com.clubflow.dto.request.RoomRequestDto;
import com.clubflow.dto.response.ApiResponse;
import com.clubflow.model.RoomRequest;
import com.clubflow.service.RoomRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/room-requests")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Room Requests", description = "Room request management API")
public class RoomRequestController {

    private final RoomRequestService roomRequestService;

    public RoomRequestController(RoomRequestService roomRequestService) {
        this.roomRequestService = roomRequestService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all room requests (Admin only)")
    public ResponseEntity<List<RoomRequest>> getAllRoomRequests() {
        List<RoomRequest> roomRequests = roomRequestService.getAllRoomRequests();
        return ResponseEntity.ok(roomRequests);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @roomRequestSecurity.isOwner(#id, principal)")
    @Operation(summary = "Get room request by ID (Admin or owner)")
    public ResponseEntity<RoomRequest> getRoomRequestById(@PathVariable Long id) {
        RoomRequest roomRequest = roomRequestService.getRoomRequestById(id);
        return ResponseEntity.ok(roomRequest);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#userId)")
    @Operation(summary = "Get room requests by user (Admin or owner)")
    public ResponseEntity<List<RoomRequest>> getRoomRequestsByUser(@PathVariable Long userId) {
        List<RoomRequest> roomRequests = roomRequestService.getRoomRequestsByUser(userId);
        return ResponseEntity.ok(roomRequests);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get room requests by status (Admin only)")
    public ResponseEntity<List<RoomRequest>> getRoomRequestsByStatus(@PathVariable String status) {
        RoomRequest.RequestStatus requestStatus = RoomRequest.RequestStatus.valueOf(status.toUpperCase());
        List<RoomRequest> roomRequests = roomRequestService.getRoomRequestsByStatus(requestStatus);
        return ResponseEntity.ok(roomRequests);
    }

    @GetMapping("/check-availability")
    @Operation(summary = "Check room availability")
    public ResponseEntity<ApiResponse> checkRoomAvailability(
            @RequestParam String roomNumber,
            @RequestParam String date,
            @RequestParam String startTime,
            @RequestParam Integer duration) {
        boolean isAvailable = roomRequestService.isRoomAvailable(roomNumber, date, startTime, duration);
        return ResponseEntity.ok(new ApiResponse(true, isAvailable ? "Room is available" : "Room is not available", isAvailable));
    }

    @PostMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Create a new room request")
    public ResponseEntity<ApiResponse> createRoomRequest(
            @PathVariable Long userId,
            @Valid @RequestBody RoomRequestDto roomRequestDto) {
        RoomRequest roomRequest = roomRequestService.createRoomRequest(userId, roomRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Room request created successfully", roomRequest));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update room request status (Admin only)")
    public ResponseEntity<ApiResponse> updateRoomRequestStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Object> statusUpdate) {
        String status = (String) statusUpdate.get("status");
        String rejectionReason = (String) statusUpdate.get("rejectionReason");

        RoomRequest.RequestStatus requestStatus = RoomRequest.RequestStatus.valueOf(status.toUpperCase());
        RoomRequest roomRequest = roomRequestService.updateRoomRequestStatus(id, requestStatus, rejectionReason);

        return ResponseEntity.ok(new ApiResponse(true, "Room request status updated successfully", roomRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete room request (Admin only)")
    public ResponseEntity<ApiResponse> deleteRoomRequest(@PathVariable Long id) {
        roomRequestService.deleteRoomRequest(id);
        return ResponseEntity.ok(new ApiResponse(true, "Room request deleted successfully"));
    }
}