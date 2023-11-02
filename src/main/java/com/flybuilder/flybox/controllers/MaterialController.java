package com.flybuilder.flybox.controllers;

import com.flybuilder.flybox.model.dto.request.MaterialInfoRequest;
import com.flybuilder.flybox.model.dto.response.MaterialInfoResponse;
import com.flybuilder.flybox.service.MaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Материалы")
@RestController
@RequestMapping("/materials")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;
    private final static String errorStr = "Материал по id %d не найден";

    @GetMapping("/{id}")
    @Operation(summary = "Получить материал по id %d")
    public MaterialInfoResponse getMaterial(@PathVariable Long id) {
        return materialService.getMaterial(id);
    }

    @GetMapping("/all")
    @Operation(summary = "Все водоёмы")
    public List<MaterialInfoResponse> getAllMaterials(){
        return materialService.getAllMaterials();
    }

    @PostMapping
    @Operation(summary = "Добавить материал")
    public MaterialInfoResponse createMaterial(@RequestBody MaterialInfoRequest request) {
        return materialService.createMaterial(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Редактировать материал")
    public MaterialInfoResponse updateMaterial(@PathVariable Long id, @RequestBody MaterialInfoRequest request) {
        return materialService.updateMaterial(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить материал")
    public void deleteMaterial(@PathVariable Long id) {
        materialService.deleteMaterial(id);
    }
}
