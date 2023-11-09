package com.flybuilder.flybox.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flybuilder.flybox.exceptions.CustomException;
import com.flybuilder.flybox.model.db.entity.User;
import com.flybuilder.flybox.model.db.repository.UserRepo;
import com.flybuilder.flybox.model.dto.request.UserInfoRequest;
import com.flybuilder.flybox.model.dto.response.UserInfoResponse;
import com.flybuilder.flybox.model.enums.Gender;
import com.flybuilder.flybox.model.enums.Role;
import com.flybuilder.flybox.model.enums.Status;
import com.flybuilder.flybox.utils.Converters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private Converters converters;

    @Mock
    private UserRepo userRepo;

    @Spy
    private ObjectMapper mapper;

    private static final Long EXISTING_ID = 1L;
    private static final Long NON_EXISTING_ID = 0L;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetUserExistingUser() {
        User existingUser = new User();
        existingUser.setId(EXISTING_ID);

        when(userRepo.findById(EXISTING_ID)).thenReturn(Optional.of(existingUser));

        UserInfoResponse response = userService.getUser(EXISTING_ID);

        assertEquals(EXISTING_ID, response.getId());
    }

    @Test
    void testGetUserWhenIdIsZero() {
        long id = 0L;

        try {
            userService.getUser(id);
            fail("Expected CustomException, but it was not thrown.");
        } catch (CustomException e) {
            assertEquals("Пользователь не найден", e.getMessage());
            assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        }
    }

    @Test
    void testGetUserNonExistingUser() {
        when(userRepo.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.getUser(NON_EXISTING_ID);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);

        List<User> userList = Arrays.asList(user1, user2);
        when(userRepo.findAll()).thenReturn(userList);
        List<UserInfoResponse> userInfoResponses = userService.getAllUsers();
        verify(userRepo).findAll();
        assertEquals(2, userInfoResponses.size());
    }

    @Test
    void testCreateUser() {

        UserInfoRequest request = new UserInfoRequest();
        request.setEmail("test@mail.ru");

        User user = new User();
        user.setId(1L);
        user.setEmail(request.getEmail());

        when(userRepo.save(any(User.class))).thenReturn(user);

        UserInfoResponse userInfoResponse = userService.createUser(request);

        verify(userRepo, times(1)).save(any(User.class));
        verify(mapper, times(1)).convertValue(eq(request), eq(User.class));
        assertNotNull(userInfoResponse);
    }


    @Test
    void testUpdateUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("oldemail@example.com");
        user.setPassword("oldpassword");
        user.setUsername("oldusername");
        user.setAge(30);
        user.setGender(Gender.MALE);
        user.setRole(Role.USER);
        user.setStatus(Status.CREATED);

        UserInfoRequest request = new UserInfoRequest();
        request.setEmail("newemail@example.com");
        request.setPassword("newpassword");
        request.setUsername("newusername");
        request.setAge(25);
        request.setGender(Gender.FEMALE);
        request.setRole(Role.ADMIN);

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(mapper.convertValue(user, UserInfoResponse.class)).thenReturn(new UserInfoResponse());

        UserInfoResponse userInfoResponse = userService.updateUser(1L, request);

        verify(userRepo, times(1)).findById(1L);
        verify(userRepo, times(1)).save(any(User.class));
        verify(mapper, times(1)).convertValue(user, UserInfoResponse.class);

        // Проверяем, что объект обновлен
        assertEquals("newemail@example.com", user.getEmail());
        assertEquals("newpassword", user.getPassword());
        assertEquals("newusername", user.getUsername());
        assertEquals(25, user.getAge().intValue());
        assertEquals(Gender.FEMALE, user.getGender());
        assertEquals(Role.ADMIN, user.getRole());
        assertEquals(Status.UPDATED, user.getStatus());
    }

    @Test
    void testUpdateUserWhenIdIsZero() {
        long id = 0L;
        UserInfoRequest request = new UserInfoRequest();

        try {
            userService.updateUser(id, request);
            fail("Expected CustomException, but it was not thrown.");
        } catch (CustomException e) {
            assertEquals("Пользователь не найден", e.getMessage());
            assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        }
    }

    @Test
    void testUpdateUserWhenUserIsNull() {
        long id = 1L;
        UserInfoRequest request = new UserInfoRequest();

        when(userRepo.findById(id)).thenReturn(Optional.empty());

        UserInfoResponse response = userService.updateUser(id, request);

        assertNull(response);
    }

    @Test
    void testDeleteUser() {
        Long userId = 1L;

        doNothing().when(userRepo).deleteById(userId);

        userService.deleteUser(userId);

        verify(userRepo, times(1)).deleteById(userId);
    }

    @Test
    void testConvertToUser() {
        UserInfoRequest userInfoRequest = new UserInfoRequest();
        userInfoRequest.setEmail("testemail@example.com");
        userInfoRequest.setPassword("testpassword");
        userInfoRequest.setUsername("testusername");
        userInfoRequest.setAge(30);
        userInfoRequest.setGender(Gender.MALE);
        userInfoRequest.setRole(Role.USER);

        User user = userService.convertToUser(userInfoRequest);

        assertEquals(userInfoRequest.getEmail(), user.getEmail());
        assertEquals(userInfoRequest.getPassword(), user.getPassword());
        assertEquals(userInfoRequest.getUsername(), user.getUsername());
        assertEquals(userInfoRequest.getAge(), user.getAge());
        assertEquals(userInfoRequest.getGender(), user.getGender());
        assertEquals(userInfoRequest.getRole(), user.getRole());
    }
}
