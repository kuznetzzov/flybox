package com.flybuilder.flybox.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flybuilder.flybox.exceptions.CustomException;
import com.flybuilder.flybox.model.db.entity.User;
import com.flybuilder.flybox.model.db.repository.UserRepo;
import com.flybuilder.flybox.model.dto.request.UserInfoRequest;
import com.flybuilder.flybox.model.dto.response.FlyInfoResponse;
import com.flybuilder.flybox.model.dto.response.UserInfoResponse;
import com.flybuilder.flybox.model.enums.Status;
import com.flybuilder.flybox.service.FlyService;
import com.flybuilder.flybox.service.UserService;
import com.flybuilder.flybox.utils.Converters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final ObjectMapper mapper;
    private final Converters converters;

    private final String errNotFound = "Пользователь не найден";

    @Override
    public UserInfoResponse getUser(Long id) {

        UserInfoResponse response;

        if (id != 0L) {
            User user = userRepo.findById(id).orElse(new User());
            response = mapper.convertValue(user, UserInfoResponse.class);
        } else {
            throw new CustomException(errNotFound, HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @Override
    public List<UserInfoResponse> getAllUsers() {
        List<User> users = userRepo.findAll();
        return users.stream().map(converters::convertToUserInfoResponse).collect(Collectors.toList());
    }

    @Override
    public UserInfoResponse createUser(UserInfoRequest request) {
        User user = mapper.convertValue(request, User.class);
        user.setCreatedAt(LocalDateTime.now());
        user.setStatus(Status.CREATED);
        User save = userRepo.save(user);
        return mapper.convertValue(save, UserInfoResponse.class);
    }

    @Override
    public UserInfoResponse updateUser(Long id, UserInfoRequest request) {

        if (id == 0L) {
            throw new CustomException(errNotFound, HttpStatus.NOT_FOUND);
        }

        User user = userRepo.findById(id).orElse(null);
        if (user == null) {
            return null;
        }

        user.setEmail(StringUtils.isBlank(request.getEmail()) ? user.getEmail() : request.getEmail());
        user.setPassword(StringUtils.isBlank(request.getPassword()) ? user.getPassword() : request.getPassword());
        user.setUsername(StringUtils.isBlank(request.getUsername()) ? user.getUsername() : request.getUsername());
        user.setAge(request.getAge() == null ? user.getAge() : request.getAge());
        user.setGender(StringUtils.isBlank(request.getGender().name()) ? user.getGender() : request.getGender());
        user.setRole(StringUtils.isBlank(request.getRole().name()) ? user.getRole() : request.getRole());

        user.setStatus(Status.UPDATED);
        user.setUpdatedAt(LocalDateTime.now());

        User save = userRepo.save(user);
        return mapper.convertValue(save, UserInfoResponse.class);
    }

    @Override
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }


    public User convertToUser(UserInfoRequest request) {

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setUsername(request.getUsername());
        user.setAge(request.getAge());
        user.setGender(request.getGender());
        user.setRole(request.getRole());

        return user;
    }

}
