package com.flybuilder.flybox.service;

import com.flybuilder.flybox.model.db.entity.Place;
import com.flybuilder.flybox.model.db.entity.User;
import com.flybuilder.flybox.model.dto.request.UserInfoRequest;
import com.flybuilder.flybox.model.dto.response.UserInfoResponse;

import java.util.List;

public interface UserService {

    UserInfoResponse getUser(Long id);

    List<UserInfoResponse> getAllUsers();

    UserInfoResponse createUser(UserInfoRequest userInfoRequest);

    UserInfoResponse updateUser(Long id, UserInfoRequest userInfoRequest);

    void deleteUser(Long id);

}
