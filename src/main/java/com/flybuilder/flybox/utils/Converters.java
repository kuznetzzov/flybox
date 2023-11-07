package com.flybuilder.flybox.utils;

import com.flybuilder.flybox.model.db.entity.*;
import com.flybuilder.flybox.model.dto.response.*;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Component
public class Converters {

    public FlyInfoResponse convertToFlyInfoResponse(Fly fly) {
        FlyInfoResponse response = FlyInfoResponse.builder()
                .id(fly.getId())
                .name(fly.getName())
                .flyType(fly.getFlyType())
                .pic(fly.getPic())
                .video(fly.getVideo())
                .status(fly.getStatus())
                .build();

        // Преобразование легенд
        Set<HistoryInfoResponse> historyInfoResponses = new HashSet<>();
        if(!fly.getHistories().isEmpty()) {
            historyInfoResponses = fly.getHistories().stream()
                    .map(this::convertToHistoryInfoResponse)
                    .collect(Collectors.toSet());
        }

        // Преобразование материалов
        Set<MaterialInfoResponse> materialInfoResponses = new HashSet<>();
        if(!fly.getMaterials().isEmpty()) {
            materialInfoResponses = fly.getMaterials().stream()
                    .map(this::convertToMaterialInfoResponse)
                    .collect(Collectors.toSet());
        }

        // Преобразование мест
        Set<PlaceInfoResponse> placeInfoResponses = new HashSet<>();
        if(!fly.getPlaces().isEmpty()) {
            placeInfoResponses = fly.getPlaces().stream()
                    .map(this::convertToPlaceInfoResponse)
                    .collect(Collectors.toSet());
        }

        // Преобразование пользователей
        Set<UserInfoResponse> userInfoResponses = new HashSet<>();
        if(!fly.getUsers().isEmpty()) {
            userInfoResponses = fly.getUsers().stream()
                    .map(this::convertToUserInfoResponse)
                    .collect(Collectors.toSet());
        }

        response.setRelatedHistories(historyInfoResponses);
        response.setRelatedMaterials(materialInfoResponses);
        response.setRelatedPlaces(placeInfoResponses);
        response.setRelatedUsers(userInfoResponses);

        return response;
    }

    public HistoryInfoResponse convertToHistoryInfoResponse(History history) {

        HistoryInfoResponse response = HistoryInfoResponse.builder()
                .id(history.getId())
                .tail(history.getTail())
                .ribbing(history.getRibbing())
                .body(history.getBody())
                .hackle(history.getHackle())
                .wing(history.getWing())
                .legs(history.getLegs())
                .head(history.getHead())
                .build();

        // Преобразование мух
        FlyInfoResponse flyInfoResponse = convertToFlyInfoResponse(history.getFly());
        response.setFly(flyInfoResponse);

        return response;
    }

    public MaterialInfoResponse convertToMaterialInfoResponse(Material material) {
        MaterialInfoResponse response = MaterialInfoResponse.builder()
                .id(material.getId())
                .name(material.getName())
                .description(material.getDescription())
                .isAvaiable(material.getIsAvaiable())
                .pic(material.getPic())
                .build();

        // Преобразование мух
        Set<FlyInfoResponse> flyInfoResponses = material.getFlies().stream()
                .map(this::convertToFlyInfoResponse)
                .collect(Collectors.toSet());

        response.setRelatedFlies(flyInfoResponses);

        return response;
    }

    public PlaceInfoResponse convertToPlaceInfoResponse(Place place) {
        PlaceInfoResponse response = PlaceInfoResponse.builder()
                .id(place.getId())
                .name(place.getName())
                .coordinates(place.getCoordinates())
                .flySinking(place.getFlySinking())
                .isSalt(place.getIsSalt())
                .build();

        // Преобразование мух
        Set<FlyInfoResponse> flyInfoResponses = place.getFlies().stream()
                .map(this::convertToFlyInfoResponse)
                .collect(Collectors.toSet());

        response.setRelatedFlies(flyInfoResponses);

        return response;
    }

    public UserInfoResponse convertToUserInfoResponse(User user) {
        UserInfoResponse response = UserInfoResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .username(user.getUsername())
                .age(user.getAge())
                .gender(user.getGender())
                .role(user.getRole())
                .build();

        // Преобразование мух
        Set<FlyInfoResponse> flyInfoResponses = user.getFlies().stream()
                .map(this::convertToFlyInfoResponse)
                .collect(Collectors.toSet());

        response.setRelatedFlies(flyInfoResponses);

        return response;
    }

}
