package com.clubflow.controller;


import com.clubflow.dto.request.MemberDto;
import com.clubflow.dto.response.ApiResponse;
import com.clubflow.model.Member;
import com.clubflow.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Members", description = "Member management API")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    @Operation(summary = "Get all members")
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get member by ID")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        Member member = memberService.getMemberById(id);
        return ResponseEntity.ok(member);
    }

    @GetMapping("/club/{club}")
    @Operation(summary = "Get members by club")
    public ResponseEntity<List<Member>> getMembersByClub(@PathVariable String club) {
        List<Member> members = memberService.getMembersByClub(club);
        return ResponseEntity.ok(members);
    }

    @GetMapping("/role/{role}")
    @Operation(summary = "Get members by role")
    public ResponseEntity<List<Member>> getMembersByRole(@PathVariable String role) {
        List<Member> members = memberService.getMembersByRole(role);
        return ResponseEntity.ok(members);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new member (Admin only)")
    public ResponseEntity<ApiResponse> createMember(@Valid @RequestBody MemberDto memberDto) {
        Member member = memberService.createMember(memberDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Member created successfully", member));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update member (Admin only)")
    public ResponseEntity<ApiResponse> updateMember(@PathVariable Long id, @Valid @RequestBody MemberDto memberDto) {
        Member member = memberService.updateMember(id, memberDto);
        return ResponseEntity.ok(new ApiResponse(true, "Member updated successfully", member));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete member (Admin only)")
    public ResponseEntity<ApiResponse> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok(new ApiResponse(true, "Member deleted successfully"));
    }
}