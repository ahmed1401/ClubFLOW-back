package com.clubflow.controller;


import com.clubflow.dto.request.GuideDto;
import com.clubflow.dto.response.ApiResponse;
import com.clubflow.model.Guide;
import com.clubflow.service.GuideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/guides")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Guides", description = "Guide management API")
public class GuideController {

    private final GuideService guideService;

    public GuideController(GuideService guideService) {
        this.guideService = guideService;
    }

    @GetMapping
    @Operation(summary = "Get all guides")
    public ResponseEntity<List<Guide>> getAllGuides() {
        List<Guide> guides = guideService.getAllGuides();
        return ResponseEntity.ok(guides);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get guide by ID")
    public ResponseEntity<Guide> getGuideById(@PathVariable Long id) {
        Guide guide = guideService.getGuideById(id);
        return ResponseEntity.ok(guide);
    }

    @GetMapping("/club/{club}")
    @Operation(summary = "Get guides by club")
    public ResponseEntity<List<Guide>> getGuidesByClub(@PathVariable String club) {
        List<Guide> guides = guideService.getGuidesByClub(club);
        return ResponseEntity.ok(guides);
    }

    @GetMapping("/search")
    @Operation(summary = "Search guides")
    public ResponseEntity<List<Guide>> searchGuides(@RequestParam String query) {
        List<Guide> guides = guideService.searchGuides(query);
        return ResponseEntity.ok(guides);
    }

    @PostMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new guide (Admin only)")
    public ResponseEntity<ApiResponse> createGuide(
            @PathVariable Long userId,
            @Valid @RequestBody GuideDto guideDto) {
        Guide guide = guideService.createGuide(userId, guideDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Guide created successfully", guide));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update guide (Admin only)")
    public ResponseEntity<ApiResponse> updateGuide(
            @PathVariable Long id,
            @Valid @RequestBody GuideDto guideDto) {
        Guide guide = guideService.updateGuide(id, guideDto);
        return ResponseEntity.ok(new ApiResponse(true, "Guide updated successfully", guide));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete guide (Admin only)")
    public ResponseEntity<ApiResponse> deleteGuide(@PathVariable Long id) {
        guideService.deleteGuide(id);
        return ResponseEntity.ok(new ApiResponse(true, "Guide deleted successfully"));
    }
}