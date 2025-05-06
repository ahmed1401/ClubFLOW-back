package com.clubflow.controller;

import com.clubflow.dto.request.MaterialRequestDto;
import com.clubflow.dto.response.ApiResponse;
import com.clubflow.model.MaterialRequest;
import com.clubflow.service.MaterialRequestService;
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
@RequestMapping("/material-requests")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Material Requests", description = "Material request management API")
public class MaterialRequestController {

    private final MaterialRequestService materialRequestService;

    public MaterialRequestController(MaterialRequestService materialRequestService) {
        this.materialRequestService = materialRequestService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all material requests (Admin only)")
    public ResponseEntity<List<MaterialRequest>> getAllMaterialRequests() {
        List<MaterialRequest> materialRequests = materialRequestService.getAllMaterialRequests();
        return ResponseEntity.ok(materialRequests);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @materialRequestSecurity.isOwner(#id, principal)")
    @Operation(summary = "Get material request by ID (Admin or owner)")
    public ResponseEntity<MaterialRequest> getMaterialRequestById(@PathVariable Long id) {
        MaterialRequest materialRequest = materialRequestService.getMaterialRequestById(id);
        return ResponseEntity.ok(materialRequest);
    }

    @GetMapping("/club/{club}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get material requests by club (Admin only)")
    public ResponseEntity<List<MaterialRequest>> getMaterialRequestsByClub(@PathVariable String club) {
        List<MaterialRequest> materialRequests = materialRequestService.getMaterialRequestsByClub(club);
        return ResponseEntity.ok(materialRequests);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#userId)")
    @Operation(summary = "Get material requests by user (Admin or owner)")
    public ResponseEntity<List<MaterialRequest>> getMaterialRequestsByUser(@PathVariable Long userId) {
        List<MaterialRequest> materialRequests = materialRequestService.getMaterialRequestsByUser(userId);
        return ResponseEntity.ok(materialRequests);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get material requests by status (Admin only)")
    public ResponseEntity<List<MaterialRequest>> getMaterialRequestsByStatus(@PathVariable String status) {
        MaterialRequest.RequestStatus requestStatus = MaterialRequest.RequestStatus.valueOf(status.toUpperCase());
        List<MaterialRequest> materialRequests = materialRequestService.getMaterialRequestsByStatus(requestStatus);
        return ResponseEntity.ok(materialRequests);
    }

    @PostMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Create a new material request")
    public ResponseEntity<ApiResponse> createMaterialRequest(
            @PathVariable Long userId,
            @Valid @RequestBody MaterialRequestDto materialRequestDto) {
        MaterialRequest materialRequest = materialRequestService.createMaterialRequest(userId, materialRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Material request created successfully", materialRequest));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update material request status (Admin only)")
    public ResponseEntity<ApiResponse> updateMaterialRequestStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Object> statusUpdate) {
        String status = (String) statusUpdate.get("status");
        String rejectionReason = (String) statusUpdate.get("rejectionReason");

        MaterialRequest.RequestStatus requestStatus = MaterialRequest.RequestStatus.valueOf(status.toUpperCase());
        MaterialRequest materialRequest = materialRequestService.updateMaterialRequestStatus(id, requestStatus, rejectionReason);

        return ResponseEntity.ok(new ApiResponse(true, "Material request status updated successfully", materialRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete material request (Admin only)")
    public ResponseEntity<ApiResponse> deleteMaterialRequest(@PathVariable Long id) {
        materialRequestService.deleteMaterialRequest(id);
        return ResponseEntity.ok(new ApiResponse(true, "Material request deleted successfully"));
    }
}