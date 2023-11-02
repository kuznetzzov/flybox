package com.flybuilder.flybox.controllers;

import com.flybuilder.flybox.model.dto.request.UserInfoRequest;
import com.flybuilder.flybox.model.dto.response.UserInfoResponse;
import com.flybuilder.flybox.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Пользователи")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final static String errorStr = "Пользователь с id %d не найден";

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по id %d")
    public UserInfoResponse getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @GetMapping("/all")
    @Operation(summary = "Все пользователи")
    public List<UserInfoResponse> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping
    @Operation(summary = "Добавить пользователя")
    public UserInfoResponse createUser(@RequestBody UserInfoRequest request) {
        return userService.createUser(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Редактировать пользователя")
    public UserInfoResponse updateUser(@PathVariable Long id, @RequestBody UserInfoRequest request) {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
