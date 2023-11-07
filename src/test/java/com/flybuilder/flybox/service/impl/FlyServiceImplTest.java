package com.flybuilder.flybox.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flybuilder.flybox.exceptions.CustomException;
import com.flybuilder.flybox.model.db.entity.Fly;
import com.flybuilder.flybox.model.db.repository.FlyRepo;
import com.flybuilder.flybox.model.dto.request.FlyInfoRequest;
import com.flybuilder.flybox.model.dto.response.FlyInfoResponse;

import com.flybuilder.flybox.model.enums.FlyType;
import com.flybuilder.flybox.model.enums.Status;
import com.flybuilder.flybox.utils.Converters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class FlyServiceImplTest {

    @InjectMocks
    private FlyServiceImpl flyService;

    @Mock
    private Converters converters;

    @Mock
    private FlyRepo flyRepo;

    @Spy
    private ObjectMapper mapper;

    private static final Long EXISTING_FLY_ID = 1L;
    private static final Long NON_EXISTING_FLY_ID = 0L;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetFlyExistingFly() {
        Fly existingFly = new Fly();
        existingFly.setId(EXISTING_FLY_ID);

        when(flyRepo.findById(EXISTING_FLY_ID)).thenReturn(Optional.of(existingFly));

        FlyInfoResponse response = flyService.getFly(EXISTING_FLY_ID);

        assertEquals(EXISTING_FLY_ID, response.getId());
    }

    @Test
    public void testGetFlyWhenIdIsZero() {
        long id = 0L;

        try {
            flyService.getFly(id);
            fail("Expected CustomException, but it was not thrown.");
        } catch (CustomException e) {
            assertEquals("Fly not found", e.getMessage());
            assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        }
    }

    @Test
    public void testGetFlyNonExistingFly() {
        when(flyRepo.findById(NON_EXISTING_FLY_ID)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            flyService.getFly(NON_EXISTING_FLY_ID);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void testGetAllFlies() {
        Fly fly1 = new Fly();
        fly1.setId(1L);
        Fly fly2 = new Fly();
        fly2.setId(2L);

        List<Fly> flyList = Arrays.asList(fly1, fly2);

        when(flyRepo.findAll()).thenReturn(flyList);

        List<FlyInfoResponse> flyInfoResponses = flyService.getAllFlies();

        verify(flyRepo).findAll();

        assertEquals(2, flyInfoResponses.size());
    }

    @Test
    public void testCreateFly() {

        FlyInfoRequest request = new FlyInfoRequest();
        request.setName("Test Fly");

        Fly fly = new Fly();
        fly.setId(1L);
        fly.setName(request.getName());

        when(flyRepo.save(any(Fly.class))).thenReturn(fly);

        FlyInfoResponse flyInfoResponse = flyService.createFly(request);

        verify(flyRepo, times(1)).save(any(Fly.class));
        verify(mapper, times(1)).convertValue(eq(request), eq(Fly.class));
        assertNotNull(flyInfoResponse);
    }


    @Test
    public void testUpdateFly() {
        Fly fly = new Fly();
        fly.setId(1L);
        fly.setName("Old Fly");
        fly.setFlyType(FlyType.DRY);
        fly.setDescription("Description");
        fly.setPic("Old Pic");
        fly.setVideo("Old Video");
        fly.setStatus(Status.CREATED);

        FlyInfoRequest request = new FlyInfoRequest();
        request.setName("New Fly");
        request.setFlyType(FlyType.NYMPH);
        request.setDescription("New Description");
        request.setPic("New Pic");
        request.setVideo("New Video");

        when(flyRepo.findById(1L)).thenReturn(Optional.of(fly));
        when(flyRepo.save(any(Fly.class))).thenReturn(fly);

        FlyInfoResponse flyInfoResponse = flyService.updateFly(1L, request);

        verify(flyRepo, times(1)).findById(1L);

        verify(flyRepo, times(1)).save(any(Fly.class));

        verify(mapper, times(1)).convertValue(fly, FlyInfoResponse.class);

        assertNotNull(flyInfoResponse);

        // Проверяем, что объект обновлен
        assertEquals("New Fly", flyInfoResponse.getName());
        assertEquals(FlyType.NYMPH, flyInfoResponse.getFlyType());
        assertEquals("New Description", flyInfoResponse.getDescription());
        assertEquals("New Pic", flyInfoResponse.getPic());
        assertEquals("New Video", flyInfoResponse.getVideo());
        assertEquals(Status.UPDATED, flyInfoResponse.getStatus());
    }

    @Test
    public void testUpdateFlyWhenIdIsZero() {
        long id = 0L;
        FlyInfoRequest request = new FlyInfoRequest();

        try {
            flyService.updateFly(id, request);
            fail("Expected CustomException, but it was not thrown.");
        } catch (CustomException e) {
            assertEquals("Fly not found", e.getMessage());
            assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        }
    }

    @Test
    public void testUpdateFlyWhenFlyIsNull() {
        long id = 1L;
        FlyInfoRequest request = new FlyInfoRequest();

        when(flyRepo.findById(id)).thenReturn(Optional.empty());

        FlyInfoResponse response = flyService.updateFly(id, request);

        assertNull(response);
    }

    @Test
    public void testDeleteFly() {
        Long flyId = 1L;

        doNothing().when(flyRepo).deleteById(flyId);

        flyService.deleteFly(flyId);

        verify(flyRepo, times(1)).deleteById(flyId);
    }

    @Test
    public void testConvertToFly() {
        FlyInfoRequest flyInfoRequest = new FlyInfoRequest();
        flyInfoRequest.setName("Test Fly");
        flyInfoRequest.setFlyType(FlyType.DRY);
        flyInfoRequest.setDescription("Description");
        flyInfoRequest.setPic("fly.jpg");
        flyInfoRequest.setVideo("fly.mp4");

        Fly fly = flyService.convertToFly(flyInfoRequest);

        assertEquals(flyInfoRequest.getName(), fly.getName());
        assertEquals(flyInfoRequest.getFlyType(), fly.getFlyType());
        assertEquals(flyInfoRequest.getDescription(), fly.getDescription());
        assertEquals(flyInfoRequest.getPic(), fly.getPic());
        assertEquals(flyInfoRequest.getVideo(), fly.getVideo());
    }
}