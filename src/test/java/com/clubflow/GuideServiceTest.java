package com.clubflow;

import com.clubflow.dto.mapper.GuideMapper;
import com.clubflow.dto.request.GuideDto;
import com.clubflow.exception.ResourceNotFoundException;
import com.clubflow.model.Guide;
import com.clubflow.model.User;
import com.clubflow.repository.GuideRepository;
import com.clubflow.repository.UserRepository;
import com.clubflow.service.GuideService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GuideServiceTest {

    private GuideRepository guideRepository;
    private UserRepository userRepository;
    private GuideMapper guideMapper;
    private GuideService guideService;

    @BeforeEach
    void setUp() {
        guideRepository = mock(GuideRepository.class);
        userRepository = mock(UserRepository.class);
        guideMapper = mock(GuideMapper.class);
        guideService = new GuideService(guideRepository, userRepository, guideMapper);
    }

    @Test
    void shouldReturnAllGuides() {
        List<Guide> guides = List.of(new Guide(), new Guide());
        when(guideRepository.findAll()).thenReturn(guides);

        List<Guide> result = guideService.getAllGuides();

        assertThat(result).hasSize(2);
        verify(guideRepository).findAll();
    }

    @Test
    void shouldReturnGuideById() {
        Guide guide = new Guide();
        guide.setId(1L);
        when(guideRepository.findById(1L)).thenReturn(Optional.of(guide));

        Guide result = guideService.getGuideById(1L);

        assertThat(result).isEqualTo(guide);
        verify(guideRepository).findById(1L);
    }

    @Test
    void shouldThrowWhenGuideNotFound() {
        when(guideRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> guideService.getGuideById(1L));
    }

    @Test
    void shouldReturnGuidesByClub() {
        List<Guide> guides = List.of(new Guide(), new Guide());
        when(guideRepository.findByClub("ENIS")).thenReturn(guides);

        List<Guide> result = guideService.getGuidesByClub("ENIS");

        assertThat(result).hasSize(2);
        verify(guideRepository).findByClub("ENIS");
    }

    @Test
    void shouldSearchGuides() {
        List<Guide> guides = List.of(new Guide());
        when(guideRepository.searchGuides("project")).thenReturn(guides);

        List<Guide> result = guideService.searchGuides("project");

        assertThat(result).hasSize(1);
        verify(guideRepository).searchGuides("project");
    }

    @Test
    void shouldCreateGuide() {
        User user = new User();
        user.setId(1L);

        GuideDto guideDto = new GuideDto();
        Guide guide = new Guide();
        Guide savedGuide = new Guide();
        savedGuide.setTitle("Example Guide");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(guideMapper.toEntity(guideDto)).thenReturn(guide);
        when(guideRepository.save(guide)).thenReturn(savedGuide);

        Guide result = guideService.createGuide(1L, guideDto);

        assertThat(result.getTitle()).isEqualTo("Example Guide");
        verify(userRepository).findById(1L);
        verify(guideRepository).save(guide);
    }

    @Test
    void shouldUpdateGuide() {
        GuideDto guideDto = new GuideDto();
        Guide guide = new Guide();
        guide.setId(1L);
        guide.setTitle("Old Title");

        when(guideRepository.findById(1L)).thenReturn(Optional.of(guide));
        doAnswer(invocation -> {
            GuideDto dto = invocation.getArgument(0);
            Guide g = invocation.getArgument(1);
            g.setTitle("New Title");
            return null;
        }).when(guideMapper).updateEntity(any(), any());

        Guide updatedGuide = new Guide();
        updatedGuide.setTitle("New Title");

        when(guideRepository.save(guide)).thenReturn(updatedGuide);

        Guide result = guideService.updateGuide(1L, guideDto);

        assertThat(result.getTitle()).isEqualTo("New Title");
        verify(guideMapper).updateEntity(guideDto, guide);
    }

    @Test
    void shouldDeleteGuide() {
        Guide guide = new Guide();
        guide.setId(1L);
        when(guideRepository.findById(1L)).thenReturn(Optional.of(guide));

        guideService.deleteGuide(1L);

        verify(guideRepository).delete(guide);
    }

    @Test
    void shouldThrowWhenDeletingNonExistentGuide() {
        when(guideRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> guideService.deleteGuide(1L));
    }
}
