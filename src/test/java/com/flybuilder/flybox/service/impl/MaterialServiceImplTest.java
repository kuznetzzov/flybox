package com.flybuilder.flybox.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flybuilder.flybox.exceptions.CustomException;
import com.flybuilder.flybox.model.db.entity.Material;
import com.flybuilder.flybox.model.db.repository.MaterialRepo;
import com.flybuilder.flybox.model.dto.request.MaterialInfoRequest;
import com.flybuilder.flybox.model.dto.response.MaterialInfoResponse;
import com.flybuilder.flybox.model.enums.Status;
import com.flybuilder.flybox.utils.Converters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class MaterialServiceImplTest {

    @InjectMocks
    private MaterialServiceImpl materialService;

    @Mock
    private Converters converters;

    @Mock
    private MaterialRepo materialRepo;

    @Spy
    private ObjectMapper mapper;

    private static final Long EXISTING_ID = 1L;
    private static final Long NON_EXISTING_ID = 0L;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetMaterialExistingMaterial() {
        Material existingMaterial = new Material();
        existingMaterial.setId(EXISTING_ID);

        when(materialRepo.findById(EXISTING_ID)).thenReturn(Optional.of(existingMaterial));

        MaterialInfoResponse response = materialService.getMaterial(EXISTING_ID);

        assertEquals(EXISTING_ID, response.getId());
    }

    @Test
    void testGetMaterialWhenIdIsZero() {
        long id = 0L;

        try {
            materialService.getMaterial(id);
            fail("Expected CustomException, but it was not thrown.");
        } catch (CustomException e) {
            assertEquals("Материал не найден", e.getMessage());
            assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        }
    }

    @Test
    void testGetMaterialNonExistingMaterial() {
        when(materialRepo.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            materialService.getMaterial(NON_EXISTING_ID);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void testGetAllMaterials() {
        Material material1 = new Material();
        material1.setId(1L);
        Material material2 = new Material();
        material2.setId(2L);

        List<Material> materialList = Arrays.asList(material1, material2);
        when(materialRepo.findAll()).thenReturn(materialList);
        List<MaterialInfoResponse> materialInfoResponses = materialService.getAllMaterials();
        verify(materialRepo).findAll();
        assertEquals(2, materialInfoResponses.size());
    }

    @Test
    void testCreateMaterial() {

        MaterialInfoRequest request = new MaterialInfoRequest();
        request.setName("Hen");

        Material material = new Material();
        material.setId(1L);
        material.setName(request.getName());

        when(materialRepo.save(any(Material.class))).thenReturn(material);

        MaterialInfoResponse materialInfoResponse = materialService.createMaterial(request);

        verify(materialRepo, times(1)).save(any(Material.class));
        verify(mapper, times(1)).convertValue(eq(request), eq(Material.class));
        assertNotNull(materialInfoResponse);
    }


    @Test
    void testUpdateMaterial() {
        Material material = new Material();
        material.setId(1L);
        material.setName("Old Material");
        material.setDescription("Old Description");
        material.setIsAvaiable(true);
        material.setPic("Old Pic");
        material.setStatus(Status.CREATED);

        MaterialInfoRequest request = new MaterialInfoRequest();
        request.setName("New Material");
        request.setDescription("New Description");
        request.setIsAvaiable(false);
        request.setPic("New Pic");

        when(materialRepo.findById(1L)).thenReturn(Optional.of(material));
        when(mapper.convertValue(material, MaterialInfoResponse.class)).thenReturn(new MaterialInfoResponse());

        MaterialInfoResponse materialInfoResponse = materialService.updateMaterial(1L, request);

        verify(materialRepo, times(1)).findById(1L);
        verify(materialRepo, times(1)).save(any(Material.class));
        verify(mapper, times(1)).convertValue(material, MaterialInfoResponse.class);

        // Проверяем, что объект обновлен
        assertEquals("New Material", material.getName());
        assertEquals("New Description", material.getDescription());
        assertEquals(false, material.getIsAvaiable());
        assertEquals("New Pic", material.getPic());
        assertEquals(Status.UPDATED, material.getStatus());
    }

    @Test
    void testUpdateMaterialWhenIdIsZero() {
        long id = 0L;
        MaterialInfoRequest request = new MaterialInfoRequest();

        try {
            materialService.updateMaterial(id, request);
            fail("Expected CustomException, but it was not thrown.");
        } catch (CustomException e) {
            assertEquals("Материал не найден", e.getMessage());
            assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        }
    }

    @Test
    void testUpdateMaterialWhenMaterialIsNull() {
        long id = 1L;
        MaterialInfoRequest request = new MaterialInfoRequest();

        when(materialRepo.findById(id)).thenReturn(Optional.empty());

        MaterialInfoResponse response = materialService.updateMaterial(id, request);

        assertNull(response);
    }

    @Test
    void testDeleteMaterial() {
        Long materialId = 1L;

        doNothing().when(materialRepo).deleteById(materialId);

        materialService.deleteMaterial(materialId);

        verify(materialRepo, times(1)).deleteById(materialId);
    }

    @Test
    void testConvertToMaterial() {
        MaterialInfoRequest materialInfoRequest = new MaterialInfoRequest();
        materialInfoRequest.setName("Test Material");
        materialInfoRequest.setDescription("Test Description");
        materialInfoRequest.setIsAvaiable(true);
        materialInfoRequest.setPic("material.jpg");

        Material material = materialService.convertToMaterial(materialInfoRequest);

        assertEquals(materialInfoRequest.getName(), material.getName());
        assertEquals(materialInfoRequest.getDescription(), material.getDescription());
        assertEquals(materialInfoRequest.getIsAvaiable(), material.getIsAvaiable());
        assertEquals(materialInfoRequest.getPic(), material.getPic());
    }
}
