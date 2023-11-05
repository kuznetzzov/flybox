package com.flybuilder.flybox.service;

import com.flybuilder.flybox.model.db.entity.Fly;
import com.flybuilder.flybox.model.dto.request.FlyInfoRequest;
import com.flybuilder.flybox.model.dto.response.FlyInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface FlyService {

    FlyInfoResponse getFly(Long id);

    List<FlyInfoResponse> getAllFlies();

    FlyInfoResponse createFly(FlyInfoRequest flyInfoRequest);

    FlyInfoResponse updateFly(Long id, FlyInfoRequest flyInfoRequest);

    void deleteFly(Long id);

}
