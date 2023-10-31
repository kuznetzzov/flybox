package com.flybox.model.dto.response;

import com.flybox.model.dto.request.FlyInfoRequest;

import java.util.Set;

public class FlyInfoResponse extends FlyInfoRequest {

    Long id;
    Set<MaterialInfoResponse> relatedMaterials;
    Set<PlaceInfoRespose> relatedPlaces;
    Set<UserInfoResponse> relatedUsers;
}
