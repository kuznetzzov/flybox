package com.flybuilder.flybox.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flybuilder.flybox.exceptions.CustomException;
import com.flybuilder.flybox.model.db.entity.*;
import com.flybuilder.flybox.model.db.repository.FlyRepo;
import com.flybuilder.flybox.model.dto.request.*;
import com.flybuilder.flybox.model.dto.response.*;
import com.flybuilder.flybox.model.enums.Status;
import com.flybuilder.flybox.service.*;
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
public class FlyServiceImpl implements FlyService {

    private final FlyRepo flyRepo;
    private final ObjectMapper mapper;

    private final HistoryService historyService;
    private final MaterialService materialService;
    private final PlaceService placeService;
    private final UserService userService;

    private final String errNotFound = "Fly with id %d not found";

    @Override
    public FlyInfoResponse getFly(Long id) {

        FlyInfoResponse response;

        if (id != null) {
            Fly fly = flyRepo.findById(id).orElse(new Fly());
            response = mapper.convertValue(fly, FlyInfoResponse.class);
        } else {
            throw new CustomException(errNotFound, HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @Override
    public List<FlyInfoResponse> getAllFlies() {
        List<Fly> flies = flyRepo.findAll();
        return flies.stream().map(this::convertToFlyInfoResponse).collect(Collectors.toList());
    }

    @Override
    public FlyInfoResponse createFly(FlyInfoRequest request) {
        Fly fly = mapper.convertValue(request, Fly.class);
        fly.setCreatedAt(LocalDateTime.now());
        fly.setStatus(Status.CREATED);
        Fly save = flyRepo.save(fly);
        return mapper.convertValue(save, FlyInfoResponse.class);
    }

    @Override
    public FlyInfoResponse updateFly(Long id, FlyInfoRequest request) {

        if (id == 0) {
            throw new CustomException(errNotFound, HttpStatus.NOT_FOUND);
        }

        Fly fly = flyRepo.findById(id).orElse(null);
        if (fly == null) {
            return null;
        }

        fly.setName(StringUtils.isBlank(request.getName()) ? fly.getName() : request.getName());
        fly.setFlyType(StringUtils.isBlank(request.getFlyType().name()) ? fly.getFlyType() : request.getFlyType());
        fly.setDescription(StringUtils.isBlank(request.getDescription()) ? fly.getDescription() : request.getDescription());
        fly.setPic(StringUtils.isBlank(request.getPic()) ? fly.getPic() : request.getPic());
        fly.setVideo(StringUtils.isBlank(request.getVideo()) ? fly.getVideo() : request.getVideo());
        fly.setStatus(Status.UPDATED);
        fly.setUpdatedAt(LocalDateTime.now());

        Fly save = flyRepo.save(fly);
        return mapper.convertValue(save, FlyInfoResponse.class);
    }

    @Override
    public void deleteFly(Long id) {
        flyRepo.deleteById(id);
    }


    @Override
    public FlyInfoResponse convertToFlyInfoResponse(Fly fly) {
        FlyInfoResponse response = FlyInfoResponse.builder()
                .id(fly.getId())
                .name(fly.getName())
                .flyType(fly.getFlyType())
                .pic(fly.getPic())
                .video(fly.getVideo())
                .build();

        // Преобразование историй
        Set<HistoryInfoResponse> historyInfoResponses = fly.getHistories().stream()
                .map(historyService::convertToHistoryInfoResponse)
                .collect(Collectors.toSet());

        // Преобразование материалов
        Set<MaterialInfoResponse> materialInfoResponses = fly.getMaterials().stream()
                .map(materialService::convertToMaterialInfoResponse)
                .collect(Collectors.toSet());

        // Преобразование мест
        Set<PlaceInfoResponse> placeInfoResponses = fly.getPlaces().stream()
                .map(placeService::convertToPlaceInfoResponse)
                .collect(Collectors.toSet());

        // Преобразование пользователей
        Set<UserInfoResponse> userInfoResponses = fly.getUsers().stream()
                .map(userService::convertToUserInfoResponse)
                .collect(Collectors.toSet());

        response.setRelatedHistories(historyInfoResponses);
        response.setRelatedMaterials(materialInfoResponses);
        response.setRelatedPlaces(placeInfoResponses);
        response.setRelatedUsers(userInfoResponses);

        return response;
    }

    @Override
    public Fly convertToFly(FlyInfoRequest flyInfoRequest) {
        Fly fly = new Fly();
        fly.setName(flyInfoRequest.getName());
        fly.setFlyType(flyInfoRequest.getFlyType());
        fly.setDescription(flyInfoRequest.getDescription());
        fly.setPic(flyInfoRequest.getPic());
        fly.setVideo(flyInfoRequest.getVideo());
        return fly;
    }

}
