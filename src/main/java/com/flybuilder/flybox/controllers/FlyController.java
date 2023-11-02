package com.flybuilder.flybox.controllers;

import com.flybuilder.flybox.model.dto.request.FlyInfoRequest;
import com.flybuilder.flybox.model.dto.response.FlyInfoResponse;
import com.flybuilder.flybox.service.FlyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Мушки")
@RestController
@RequestMapping("/flies")
@RequiredArgsConstructor
public class FlyController {

    private final FlyService flyService;
    private final static String errorStr = "Мушка с id %d не найдена";

    @GetMapping("/{id}")
    @Operation(summary = "Получить мушку по id %d")
    public FlyInfoResponse getFly(@PathVariable Long id) {
        return flyService.getFly(id);
    }

    @GetMapping("/all")
    @Operation(summary = "Все мушки")
    public List<FlyInfoResponse> getAllFlies(){
        return flyService.getAllFlies();
    }

    @PostMapping
    @Operation(summary = "Добавить мушку")
    public FlyInfoResponse createFly(@RequestBody FlyInfoRequest request) {
        return flyService.createFly(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Редактировать мушку")
    public FlyInfoResponse updateFly(@PathVariable Long id, @RequestBody FlyInfoRequest request) {
        return flyService.updateFly(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить мушку")
    public void deleteFly(@PathVariable Long id) {
        flyService.deleteFly(id);
    }
}
