package com.clubflow;

import com.clubflow.dto.mapper.MemberMapper;
import com.clubflow.dto.request.MemberDto;
import com.clubflow.exception.ResourceNotFoundException;
import com.clubflow.model.Member;
import com.clubflow.repository.MemberRepository;

import com.clubflow.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    private MemberRepository memberRepository;
    private MemberMapper memberMapper;
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberRepository = mock(MemberRepository.class);
        memberMapper = mock(MemberMapper.class);
        memberService = new MemberService(memberRepository, memberMapper);
    }

    @Test
    void testGetAllMembers_success() {
        List<Member> mockMembers = Arrays.asList(new Member(), new Member());

        when(memberRepository.findAll()).thenReturn(mockMembers);

        List<Member> result = memberService.getAllMembers();

        assertEquals(2, result.size());
        verify(memberRepository).findAll();
    }

    @Test
    void testGetMemberById_found() {
        Member member = new Member();
        member.setId(1L);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        Member result = memberService.getMemberById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void testGetMemberById_notFound() {
        when(memberRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> memberService.getMemberById(99L));
    }

    @Test
    void testGetMembersByClub_success() {
        List<Member> members = List.of(new Member());
        when(memberRepository.findByClub("TechClub")).thenReturn(members);

        List<Member> result = memberService.getMembersByClub("TechClub");

        assertEquals(1, result.size());
        verify(memberRepository).findByClub("TechClub");
    }

    @Test
    void testGetMembersByRole_success() {
        List<Member> members = List.of(new Member());
        when(memberRepository.findByRole("President")).thenReturn(members);

        List<Member> result = memberService.getMembersByRole("President");

        assertEquals(1, result.size());
        verify(memberRepository).findByRole("President");
    }

    @Test
    void testCreateMember_success() {
        MemberDto dto = new MemberDto("Ahmed", "Hajje", "ahmed@gmail.com", "12345678",
                "img.jpg", "TechClub", "President", "2001-01-01", "Tunis", "link.com");

        Member mapped = new Member();
        mapped.setEmail(dto.getEmail());

        when(memberRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(memberMapper.toEntity(dto)).thenReturn(mapped);
        when(memberRepository.save(mapped)).thenReturn(mapped);

        Member result = memberService.createMember(dto);

        assertEquals(dto.getEmail(), result.getEmail());
        verify(memberRepository).save(mapped);
    }

    @Test
    void testCreateMember_emailExists_shouldThrowException() {
        MemberDto dto = new MemberDto();
        dto.setEmail("existing@example.com");

        when(memberRepository.existsByEmail("existing@example.com")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> memberService.createMember(dto));

        assertEquals("Email is already taken!", exception.getMessage());
    }

    @Test
    void testUpdateMember_success() {
        Long id = 1L;

        Member existing = new Member();
        existing.setId(id);
        existing.setEmail("old@example.com");

        MemberDto dto = new MemberDto();
        dto.setEmail("new@example.com");

        when(memberRepository.findById(id)).thenReturn(Optional.of(existing));
        when(memberRepository.existsByEmail("new@example.com")).thenReturn(false);
        doAnswer(inv -> {
            existing.setEmail("new@example.com");
            return null;
        }).when(memberMapper).updateEntity(dto, existing);
        when(memberRepository.save(existing)).thenReturn(existing);

        Member result = memberService.updateMember(id, dto);

        assertEquals("new@example.com", result.getEmail());
    }

    @Test
    void testUpdateMember_emailAlreadyExists_shouldThrowException() {
        Long id = 2L;
        Member existing = new Member();
        existing.setId(id);
        existing.setEmail("old@example.com");

        MemberDto dto = new MemberDto();
        dto.setEmail("existing@example.com");

        when(memberRepository.findById(id)).thenReturn(Optional.of(existing));
        when(memberRepository.existsByEmail("existing@example.com")).thenReturn(true);

        assertThrows(RuntimeException.class,
                () -> memberService.updateMember(id, dto));
    }

    @Test
    void testUpdateMember_notFound_shouldThrowException() {
        when(memberRepository.findById(404L)).thenReturn(Optional.empty());

        MemberDto dto = new MemberDto();
        assertThrows(ResourceNotFoundException.class,
                () -> memberService.updateMember(404L, dto));
    }

    @Test
    void testDeleteMember_success() {
        Member member = new Member();
        member.setId(5L);
        member.setEmail("test@test.com");

        when(memberRepository.findById(5L)).thenReturn(Optional.of(member));

        memberService.deleteMember(5L);

        verify(memberRepository).delete(member);
    }

    @Test
    void testDeleteMember_notFound_shouldThrowException() {
        when(memberRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> memberService.deleteMember(404L));
    }
}
