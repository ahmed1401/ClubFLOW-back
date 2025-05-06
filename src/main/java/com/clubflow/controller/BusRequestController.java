package com.clubflow.controller;


import com.clubflow.dto.request.BusRequestDto;
import com.clubflow.dto.response.ApiResponse;
import com.clubflow.model.BusRequest;
import com.clubflow.service.BusRequestService;
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
@RequestMapping("/bus-requests")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Bus Requests", description = "Bus request management API")
public class BusRequestController {

    private final BusRequestService busRequestService;

    public BusRequestController(BusRequestService busRequestService) {
        this.busRequestService = busRequestService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all bus requests (Admin only)")
    public ResponseEntity<List<BusRequest>> getAllBusRequests() {
        List<BusRequest> busRequests = busRequestService.getAllBusRequests();
        return ResponseEntity.ok(busRequests);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @busRequestSecurity.isOwner(#id, principal)")
    @Operation(summary = "Get bus request by ID (Admin or owner)")
    public ResponseEntity<BusRequest> getBusRequestById(@PathVariable Long id) {
        BusRequest busRequest = busRequestService.getBusRequestById(id);
        return ResponseEntity.ok(busRequest);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#userId)")
    @Operation(summary = "Get bus requests by user (Admin or owner)")
    public ResponseEntity<List<BusRequest>> getBusRequestsByUser(@PathVariable Long userId) {
        List<BusRequest> busRequests = busRequestService.getBusRequestsByUser(userId);
        return ResponseEntity.ok(busRequests);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get bus requests by status (Admin only)")
    public ResponseEntity<List<BusRequest>> getBusRequestsByStatus(@PathVariable String status) {
        BusRequest.RequestStatus requestStatus = BusRequest.RequestStatus.valueOf(status.toUpperCase());
        List<BusRequest> busRequests = busRequestService.getBusRequestsByStatus(requestStatus);
        return ResponseEntity.ok(busRequests);
    }

    @PostMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Create a new bus request")
    public ResponseEntity<ApiResponse> createBusRequest(
            @PathVariable Long userId,
            @Valid @RequestBody BusRequestDto busRequestDto) {
        BusRequest busRequest = busRequestService.createBusRequest(userId, busRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Bus request created successfully", busRequest));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update bus request status (Admin only)")
    public ResponseEntity<ApiResponse> updateBusRequestStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Object> statusUpdate) {
        String status = (String) statusUpdate.get("status");
        String rejectionReason = (String) statusUpdate.get("rejectionReason");

        BusRequest.RequestStatus requestStatus = BusRequest.RequestStatus.valueOf(status.toUpperCase());
        BusRequest busRequest = busRequestService.updateBusRequestStatus(id, requestStatus, rejectionReason);

        return ResponseEntity.ok(new ApiResponse(true, "Bus request status updated successfully", busRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete bus request (Admin only)")
    public ResponseEntity<ApiResponse> deleteBusRequest(@PathVariable Long id) {
        busRequestService.deleteBusRequest(id);
        return ResponseEntity.ok(new ApiResponse(true, "Bus request deleted successfully"));
    }
}