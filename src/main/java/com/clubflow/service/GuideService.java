package com.clubflow.service;


import com.clubflow.dto.mapper.GuideMapper;
import com.clubflow.dto.request.GuideDto;
import com.clubflow.exception.ResourceNotFoundException;
import com.clubflow.model.Guide;
import com.clubflow.model.User;
import com.clubflow.repository.GuideRepository;
import com.clubflow.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GuideService {
    private static final Logger logger = LoggerFactory.getLogger(GuideService.class);

    private final GuideRepository guideRepository;
    private final UserRepository userRepository;
    private final GuideMapper guideMapper;

    public GuideService(GuideRepository guideRepository,
                        UserRepository userRepository,
                        GuideMapper guideMapper) {
        this.guideRepository = guideRepository;
        this.userRepository = userRepository;
        this.guideMapper = guideMapper;
    }

    public List<Guide> getAllGuides() {
        return guideRepository.findAll();
    }

    public Guide getGuideById(Long id) {
        return guideRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guide", "id", id));
    }

    public List<Guide> getGuidesByClub(String club) {
        return guideRepository.findByClub(club);
    }

    public List<Guide> searchGuides(String query) {
        return guideRepository.searchGuides(query);
    }

    @Transactional
    public Guide createGuide(Long userId, GuideDto guideDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Guide guide = guideMapper.toEntity(guideDto);
        guide.setCreatedBy(user);

        Guide savedGuide = guideRepository.save(guide);
        logger.info("Guide created successfully: {}", savedGuide.getTitle());
        return savedGuide;
    }

    @Transactional
    public Guide updateGuide(Long id, GuideDto guideDto) {
        Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guide", "id", id));

        guideMapper.updateEntity(guideDto, guide);
        Guide updatedGuide = guideRepository.save(guide);
        logger.info("Guide updated successfully: {}", updatedGuide.getTitle());
        return updatedGuide;
    }

    @Transactional
    public void deleteGuide(Long id) {
        Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guide", "id", id));
        guideRepository.delete(guide);
        logger.info("Guide deleted successfully: {}", id);
    }
}