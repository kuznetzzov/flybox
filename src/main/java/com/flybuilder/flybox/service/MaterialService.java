package com.flybuilder.flybox.service;

import com.flybuilder.flybox.model.db.entity.Fly;
import com.flybuilder.flybox.model.db.entity.Material;
import com.flybuilder.flybox.model.dto.request.FlyInfoRequest;
import com.flybuilder.flybox.model.dto.request.MaterialInfoRequest;
import com.flybuilder.flybox.model.dto.response.FlyInfoResponse;
import com.flybuilder.flybox.model.dto.response.MaterialInfoResponse;

import java.util.List;

public interface MaterialService {

    MaterialInfoResponse getMaterial(Long id);

    List<MaterialInfoResponse> getAllMaterials();

    MaterialInfoResponse createMaterial(MaterialInfoRequest materialInfoRequest);

    MaterialInfoResponse updateMaterial(Long id, MaterialInfoRequest materialInfoRequest);

    void deleteMaterial(Long id);

}
