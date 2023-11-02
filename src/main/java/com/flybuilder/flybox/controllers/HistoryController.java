package com.flybuilder.flybox.controllers;

import com.flybuilder.flybox.model.dto.request.HistoryInfoRequest;
import com.flybuilder.flybox.model.dto.response.HistoryInfoResponse;
import com.flybuilder.flybox.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Histories")
@RestController
@RequestMapping("/histories")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;
    private final static String errorStr = "Легенда по id %d не найдена";

    @GetMapping("/{id}")
    @Operation(summary = "Получить легенду по id")
    public HistoryInfoResponse getHistory(@PathVariable Long id) {
        return historyService.getHistory(id);
    }

    @GetMapping("/all")
    @Operation(summary = "Все легенды")
    public List<HistoryInfoResponse> getAllHistories(){
        return historyService.getAllHistories();
    }

    @PostMapping
    @Operation(summary = "Добавить легенду")
    public HistoryInfoResponse createHistory(@RequestBody HistoryInfoRequest request) {
        return historyService.createHistory(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Редактировать легенду")
    public HistoryInfoResponse updateHistory(@PathVariable Long id, @RequestBody HistoryInfoRequest request) {
        return historyService.updateHistory(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить легенду")
    public void deleteHistory(@PathVariable Long id) {
        historyService.deleteHistory(id);
    }
}
