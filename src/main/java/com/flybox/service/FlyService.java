package com.flybox.service;

import com.flybox.model.dto.request.FlyInfoRequest;
import com.flybox.model.dto.response.FlyInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface FlyService {

    FlyInfoResponse getFly(Long id);

    Page<FlyInfoResponse> getAllFlies(Integer page, Integer perPage, String sort, Sort.Direction order, String filter);

    FlyInfoResponse createFly(FlyInfoRequest flyInfoRequest);

    FlyInfoResponse updateFly(Long id, FlyInfoRequest flyInfoRequest);

    void deleteFly(Long id);


}
