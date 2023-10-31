package com.flybox.model.dto.response;

import com.flybox.model.dto.request.UserInfoRequest;

import java.util.Set;

public class UserInfoResponse extends UserInfoRequest {

    Long id;
    Set<FlyInfoResponse> relatedFlies;
}
