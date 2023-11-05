package com.flybuilder.flybox.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flybuilder.flybox.exceptions.CustomException;
import com.flybuilder.flybox.model.db.entity.Place;
import com.flybuilder.flybox.model.db.repository.PlaceRepo;
import com.flybuilder.flybox.model.dto.request.PlaceInfoRequest;
import com.flybuilder.flybox.model.dto.response.FlyInfoResponse;
import com.flybuilder.flybox.model.dto.response.PlaceInfoResponse;
import com.flybuilder.flybox.model.enums.Status;
import com.flybuilder.flybox.service.FlyService;
import com.flybuilder.flybox.service.PlaceService;
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
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepo placeRepo;
    private final ObjectMapper mapper;
    private final Converters converters;


    private final String errNotFound = "Material with id %d not found";

    @Override
    public PlaceInfoResponse getPlace(Long id) {

        PlaceInfoResponse response;

        if (id != null) {
            Place place = placeRepo.findById(id).orElse(new Place());
            response = mapper.convertValue(place, PlaceInfoResponse.class);
        } else {
            throw new CustomException(errNotFound, HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @Override
    public List<PlaceInfoResponse> getAllPlaces() {
        List<Place> places = placeRepo.findAll();
        return places.stream().map(converters::convertToPlaceInfoResponse).collect(Collectors.toList());
    }

    @Override
    public PlaceInfoResponse createPlace(PlaceInfoRequest request) {
        Place place = mapper.convertValue(request, Place.class);
        place.setCreatedAt(LocalDateTime.now());
        place.setStatus(Status.CREATED);
        Place save = placeRepo.save(place);
        return mapper.convertValue(save, PlaceInfoResponse.class);
    }

    @Override
    public PlaceInfoResponse updatePlace(Long id, PlaceInfoRequest request) {

        if (id == 0) {
            throw new CustomException(errNotFound, HttpStatus.NOT_FOUND);
        }

        Place place = placeRepo.findById(id).orElse(null);
        if (place == null) {
            return null;
        }

        place.setName(StringUtils.isBlank(request.getName()) ? place.getName() : request.getName());
        place.setCoordinates(StringUtils.isBlank(request.getCoordinates()) ? place.getCoordinates() : request.getCoordinates());
        place.setFlySinking(request.getFlySinking() == null ? place.getFlySinking() : request.getFlySinking());
        place.setIsSalt(request.getIsSalt() == null ? place.getIsSalt() : request.getIsSalt());

        place.setStatus(Status.UPDATED);
        place.setUpdatedAt(LocalDateTime.now());

        Place save = placeRepo.save(place);
        return mapper.convertValue(save, PlaceInfoResponse.class);
    }

    @Override
    public void deletePlace(Long id) {
        placeRepo.deleteById(id);
    }

    public Place convertToPlace(PlaceInfoRequest placeInfoRequest) {
        Place place = new Place();
        place.setName(placeInfoRequest.getName());
        place.setCoordinates(placeInfoRequest.getCoordinates());
        place.setFlySinking(placeInfoRequest.getFlySinking());
        place.setIsSalt(placeInfoRequest.getIsSalt());

        return place;
    }

}
