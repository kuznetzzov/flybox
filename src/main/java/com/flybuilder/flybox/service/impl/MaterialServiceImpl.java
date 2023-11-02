package com.flybuilder.flybox.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flybuilder.flybox.exceptions.CustomException;
import com.flybuilder.flybox.model.db.entity.*;
import com.flybuilder.flybox.model.db.repository.MaterialRepo;
import com.flybuilder.flybox.model.dto.request.*;
import com.flybuilder.flybox.model.dto.response.FlyInfoResponse;
import com.flybuilder.flybox.model.dto.response.MaterialInfoResponse;
import com.flybuilder.flybox.model.enums.Status;
import com.flybuilder.flybox.service.FlyService;
import com.flybuilder.flybox.service.MaterialService;
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
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepo materialRepo;
    private final ObjectMapper mapper;
    private final FlyService flyService;

    private final String errNotFound = "Material with id %d not found";

    @Override
    public MaterialInfoResponse getMaterial(Long id) {

        MaterialInfoResponse response;

        if (id != null) {
            Material material = materialRepo.findById(id).orElse(new Material());
            response = mapper.convertValue(material, MaterialInfoResponse.class);
        } else {
            throw new CustomException(errNotFound, HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @Override
    public List<MaterialInfoResponse> getAllMaterials() {
        List<Material> materials = materialRepo.findAll();
        return materials.stream().map(this::convertToMaterialInfoResponse).collect(Collectors.toList());
    }

    @Override
    public MaterialInfoResponse createMaterial(MaterialInfoRequest request) {
        Material material = mapper.convertValue(request, Material.class);
        material.setCreatedAt(LocalDateTime.now());
        material.setStatus(Status.CREATED);
        Material save = materialRepo.save(material);
        return mapper.convertValue(save, MaterialInfoResponse.class);
    }

    @Override
    public MaterialInfoResponse updateMaterial(Long id, MaterialInfoRequest request) {

        if (id == 0) {
            throw new CustomException(errNotFound, HttpStatus.NOT_FOUND);
        }

        Material material = materialRepo.findById(id).orElse(null);
        if (material == null) {
            return null;
        }

        material.setName(StringUtils.isBlank(request.getName()) ? material.getName() : request.getName());
        material.setDescription(StringUtils.isBlank(request.getDescription()) ? material.getDescription() : request.getDescription());
        material.setIsAvaiable(request.getIsAvaiable() == null ? material.getIsAvaiable() : request.getIsAvaiable());
        material.setPic(StringUtils.isBlank(request.getPic()) ? material.getPic() : request.getPic());

        material.setStatus(Status.UPDATED);
        material.setUpdatedAt(LocalDateTime.now());

        Material save = materialRepo.save(material);
        return mapper.convertValue(save, MaterialInfoResponse.class);
    }

    @Override
    public void deleteMaterial(Long id) {
        materialRepo.deleteById(id);
    }


    @Override
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
                .map(flyService::convertToFlyInfoResponse)
                .collect(Collectors.toSet());

        response.setRelatedFlies(flyInfoResponses);

        return response;
    }

    @Override
    public Material convertToMaterial(MaterialInfoRequest materialInfoRequest) {
        Material material = new Material();
        material.setName(materialInfoRequest.getName());
        material.setDescription(materialInfoRequest.getDescription());
        material.setIsAvaiable(materialInfoRequest.getIsAvaiable());
        material.setPic(materialInfoRequest.getPic());

        return material;
    }

}
