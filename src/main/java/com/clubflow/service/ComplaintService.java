package com.clubflow.service;

import com.clubflow.dto.mapper.ComplaintMapper;
import com.clubflow.dto.request.ComplaintDto;
import com.clubflow.exception.ResourceNotFoundException;
import com.clubflow.model.Complaint;
import com.clubflow.model.User;
import com.clubflow.repository.ComplaintRepository;
import com.clubflow.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ComplaintService {
    private static final Logger logger = LoggerFactory.getLogger(ComplaintService.class);

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final ComplaintMapper complaintMapper;

    public ComplaintService(ComplaintRepository complaintRepository,
                            UserRepository userRepository,
                            ComplaintMapper complaintMapper) {
        this.complaintRepository = complaintRepository;
        this.userRepository = userRepository;
        this.complaintMapper = complaintMapper;
    }

    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

    public Complaint getComplaintById(Long id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint", "id", id));
    }

    public List<Complaint> getComplaintsByClub(String club) {
        return complaintRepository.findByClub(club);
    }

    public List<Complaint> getComplaintsBySubject(String subject) {
        return complaintRepository.findBySubject(subject);
    }

    public List<Complaint> getComplaintsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return complaintRepository.findBySubmittedBy(user);
    }

    public List<Complaint> getComplaintsByStatus(Complaint.ComplaintStatus status) {
        return complaintRepository.findByStatus(status);
    }

    @Transactional
    public Complaint createComplaint(Long userId, ComplaintDto complaintDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Complaint complaint = complaintMapper.toEntity(complaintDto);
        complaint.setSubmittedBy(user);
        complaint.setStatus(Complaint.ComplaintStatus.PENDING);

        Complaint savedComplaint = complaintRepository.save(complaint);
        logger.info("Complaint created successfully for club: {}", complaint.getClub());
        return savedComplaint;
    }

    @Transactional
    public Complaint updateComplaintStatus(Long id, Complaint.ComplaintStatus status, String response) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint", "id", id));

        complaint.setStatus(status);
        if (response != null) {
            complaint.setResponse(response);
        }

        Complaint updatedComplaint = complaintRepository.save(complaint);
        logger.info("Complaint status updated to {} for complaint ID: {}", status, id);
        return updatedComplaint;
    }

    @Transactional
    public void deleteComplaint(Long id) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint", "id", id));
        complaintRepository.delete(complaint);
        logger.info("Complaint deleted successfully: {}", id);
    }
}