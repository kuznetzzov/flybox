package com.flybox.controllers;

import com.flybox.exceptions.CustomException;
import com.flybox.model.dto.request.FlyInfoRequest;
import com.flybox.model.dto.response.FlyInfoResponse;
import com.flybox.service.FlyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Tag(name = "Flies")
@RestController
@RequestMapping("/flies")
@RequiredArgsConstructor
public class FlyController {

    private final FlyService flyService;
    private final static String errorStr = "Car with id %d not found";

    @GetMapping("/{id}")
    @Operation(summary = "Get fly by id")
    public FlyInfoResponse getFly(@PathVariable Long id) {
        return flyService.getFly(id);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all flies")
    public Page<FlyInfoResponse> getAllFlies (@RequestParam(defaultValue = "1") Integer page,
                                             @RequestParam(defaultValue = "10") Integer perPage,
                                             @RequestParam(defaultValue = "flyType") String sort,
                                             @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                             @RequestParam(required = false) String filter) {
        return flyService.getAllFlies(page, perPage, sort, order, filter);
    }

    @PostMapping
    @Operation(summary = "Create fly")
    public FlyInfoResponse createFly(@RequestBody FlyInfoRequest request) {
        return flyService.createFly(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update fly")
    public FlyInfoResponse updateFly(@PathVariable Long id, @RequestBody FlyInfoRequest request) {
        return flyService.updateFly(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete fly")
    public void deleteFly(@PathVariable Long id) {
        flyService.deleteFly(id);
    }
}
