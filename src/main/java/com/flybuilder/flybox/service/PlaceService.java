package com.flybuilder.flybox.service;

import com.flybuilder.flybox.model.db.entity.Place;
import com.flybuilder.flybox.model.dto.request.PlaceInfoRequest;
import com.flybuilder.flybox.model.dto.response.PlaceInfoResponse;

import java.util.List;

public interface PlaceService {

    PlaceInfoResponse getPlace(Long id);

    List<PlaceInfoResponse> getAllPlaces();

    PlaceInfoResponse createPlace(PlaceInfoRequest placeInfoRequest);

    PlaceInfoResponse updatePlace(Long id, PlaceInfoRequest placeInfoRequest);

    void deletePlace(Long id);

}
