package com.clubflow.repository;


import com.clubflow.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    List<Member> findByClub(String club);
    List<Member> findByRole(String role);
    Boolean existsByEmail(String email);
}