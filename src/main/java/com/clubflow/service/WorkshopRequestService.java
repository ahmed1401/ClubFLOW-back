package com.clubflow.service;


import com.clubflow.dto.mapper.WorkshopRequestMapper;
import com.clubflow.dto.request.WorkshopRequestDto;
import com.clubflow.exception.ResourceNotFoundException;
import com.clubflow.model.Member;
import com.clubflow.model.WorkshopRequest;
import com.clubflow.repository.MemberRepository;
import com.clubflow.repository.WorkshopRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkshopRequestService {
    private static final Logger logger = LoggerFactory.getLogger(WorkshopRequestService.class);

    private final WorkshopRequestRepository workshopRequestRepository;
    private final MemberRepository memberRepository;
    private final WorkshopRequestMapper workshopRequestMapper;

    public WorkshopRequestService(WorkshopRequestRepository workshopRequestRepository,
                                  MemberRepository memberRepository,
                                  WorkshopRequestMapper workshopRequestMapper) {
        this.workshopRequestRepository = workshopRequestRepository;
        this.memberRepository = memberRepository;
        this.workshopRequestMapper = workshopRequestMapper;
    }

    public List<WorkshopRequest> getAllWorkshopRequests() {
        return workshopRequestRepository.findAll();
    }

    public WorkshopRequest getWorkshopRequestById(Long id) {
        return workshopRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WorkshopRequest", "id", id));
    }

    public List<WorkshopRequest> getWorkshopRequestsByMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", memberId));
        return workshopRequestRepository.findByMember(member);
    }

    public List<WorkshopRequest> getWorkshopRequestsByStatus(WorkshopRequest.RequestStatus status) {
        return workshopRequestRepository.findByStatus(status);
    }

    @Transactional
    public WorkshopRequest createWorkshopRequest(WorkshopRequestDto workshopRequestDto) {
        Member member = null;
        if (workshopRequestDto.getMemberId() != null) {
            member = memberRepository.findById(workshopRequestDto.getMemberId())
                    .orElseThrow(() -> new ResourceNotFoundException("Member", "id", workshopRequestDto.getMemberId()));
        }

        WorkshopRequest workshopRequest = workshopRequestMapper.toEntity(workshopRequestDto);
        workshopRequest.setMember(member);
        workshopRequest.setStatus(WorkshopRequest.RequestStatus.PENDING);

        WorkshopRequest savedRequest = workshopRequestRepository.save(workshopRequest);
        logger.info("Workshop request created successfully: {}", savedRequest.getTitle());
        return savedRequest;
    }

    @Transactional
    public WorkshopRequest updateWorkshopRequestStatus(Long id, WorkshopRequest.RequestStatus status, String rejectionReason) {
        WorkshopRequest workshopRequest = workshopRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WorkshopRequest", "id", id));

        workshopRequest.setStatus(status);
        if (status == WorkshopRequest.RequestStatus.REJECTED && rejectionReason != null) {
            workshopRequest.setRejectionReason(rejectionReason);
        }

        WorkshopRequest updatedRequest = workshopRequestRepository.save(workshopRequest);
        logger.info("Workshop request status updated to {} for request ID: {}", status, id);
        return updatedRequest;
    }

    @Transactional
    public void deleteWorkshopRequest(Long id) {
        WorkshopRequest workshopRequest = workshopRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WorkshopRequest", "id", id));
        workshopRequestRepository.delete(workshopRequest);
        logger.info("Workshop request deleted successfully: {}", id);
    }
}