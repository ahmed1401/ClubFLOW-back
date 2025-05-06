package com.clubflow.service;


import com.clubflow.dto.mapper.MemberMapper;
import com.clubflow.dto.request.MemberDto;
import com.clubflow.exception.ResourceNotFoundException;
import com.clubflow.model.Member;
import com.clubflow.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberService {
    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    public MemberService(MemberRepository memberRepository, MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", id));
    }

    public List<Member> getMembersByClub(String club) {
        return memberRepository.findByClub(club);
    }

    public List<Member> getMembersByRole(String role) {
        return memberRepository.findByRole(role);
    }

    @Transactional
    public Member createMember(MemberDto memberDto) {
        if (memberRepository.existsByEmail(memberDto.getEmail())) {
            logger.error("Email is already taken: {}", memberDto.getEmail());
            throw new RuntimeException("Email is already taken!");
        }

        Member member = memberMapper.toEntity(memberDto);
        Member savedMember = memberRepository.save(member);
        logger.info("Member created successfully: {}", savedMember.getEmail());
        return savedMember;
    }

    @Transactional
    public Member updateMember(Long id, MemberDto memberDto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", id));

        // Check if email is being changed and if it already exists
        if (!member.getEmail().equals(memberDto.getEmail()) &&
                memberRepository.existsByEmail(memberDto.getEmail())) {
            logger.error("Email is already taken: {}", memberDto.getEmail());
            throw new RuntimeException("Email is already taken!");
        }

        memberMapper.updateEntity(memberDto, member);
        Member updatedMember = memberRepository.save(member);
        logger.info("Member updated successfully: {}", updatedMember.getEmail());
        return updatedMember;
    }

    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", id));
        memberRepository.delete(member);
        logger.info("Member deleted successfully: {}", member.getEmail());
    }
}