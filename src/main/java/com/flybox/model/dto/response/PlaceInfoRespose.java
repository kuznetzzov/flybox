package com.flybox.model.dto.response;

import com.flybox.model.dto.request.PlaceInfoRequest;

import java.util.Set;

public class PlaceInfoRespose extends PlaceInfoRequest {

    Long id;
    Set<FlyInfoResponse> relatedFlies;
}
