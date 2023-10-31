package com.flybox.model.dto.response;

import com.flybox.model.dto.request.MaterialInfoRequest;

import java.util.Set;

public class MaterialInfoResponse extends MaterialInfoRequest {

    Long id;
    Set<FlyInfoResponse> relatedFlies;
}
