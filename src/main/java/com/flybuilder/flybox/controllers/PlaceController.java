package com.flybuilder.flybox.controllers;

import com.flybuilder.flybox.model.dto.request.PlaceInfoRequest;
import com.flybuilder.flybox.model.dto.response.PlaceInfoResponse;
import com.flybuilder.flybox.service.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Водоёмы")
@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;
    private final static String errorStr = "Водоём по id %d не найден";

    @GetMapping("/{id}")
    @Operation(summary = "Получить водоём по id %d")
    public PlaceInfoResponse getPlace(@PathVariable Long id) {
        return placeService.getPlace(id);
    }

    @GetMapping("/all")
    @Operation(summary = "Все водоёмы")
    public List<PlaceInfoResponse> getAllPlaces(){
        return placeService.getAllPlaces();
    }

    @PostMapping
    @Operation(summary = "Добавить водоём")
    public PlaceInfoResponse createPlace(@RequestBody PlaceInfoRequest request) {
        return placeService.createPlace(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Редактировать водоём")
    public PlaceInfoResponse updatePlace(@PathVariable Long id, @RequestBody PlaceInfoRequest request) {
        return placeService.updatePlace(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить водоём")
    public void deletePlace(@PathVariable Long id) {
        placeService.deletePlace(id);
    }
}
