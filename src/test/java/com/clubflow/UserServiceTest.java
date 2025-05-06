package com.clubflow;

import com.clubflow.dto.mapper.UserMapper;
import com.clubflow.dto.request.UserDto;
import com.clubflow.exception.ResourceNotFoundException;
import com.clubflow.model.Role;
import com.clubflow.model.User;
import com.clubflow.repository.RoleRepository;
import com.clubflow.repository.UserRepository;
import com.clubflow.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private AutoCloseable closeable;

    private UserDto userDto;
    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        userDto = new UserDto();
        userDto.setEmail("test@clubflow.com");
        userDto.setPassword("password");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");

        user = new User();
        user.setId(1L);
        user.setEmail(userDto.getEmail());
        user.setPassword("encodedPass");
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());

        role = new Role();
        role.setId(1L);
        role.setName(Role.ERole.ROLE_USER);
    }

    @Test
    void testCreateUser_Success() {
        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(false);
        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPass");
        when(roleRepository.findByName(Role.ERole.ROLE_USER)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.createUser(userDto);

        assertNotNull(savedUser);
        assertEquals("test@clubflow.com", savedUser.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testCreateUser_EmailExists() {
        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.createUser(userDto));

        assertEquals("Email is already taken!", exception.getMessage());
    }

    @Test
    void testGetUserByEmail_Success() {
        when(userRepository.findByEmail("test@clubflow.com")).thenReturn(Optional.of(user));

        User foundUser = userService.getUserByEmail("test@clubflow.com");

        assertNotNull(foundUser);
        assertEquals("test@clubflow.com", foundUser.getEmail());
    }

    @Test
    void testGetUserByEmail_NotFound() {
        when(userRepository.findByEmail("nonexistent@clubflow.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserByEmail("nonexistent@clubflow.com"));
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository).delete(user);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.deleteUser(99L));
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}
