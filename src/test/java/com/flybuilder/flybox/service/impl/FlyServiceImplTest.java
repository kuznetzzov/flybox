package com.flybuilder.flybox.service.impl;

import com.flybuilder.flybox.exceptions.CustomException;
import com.flybuilder.flybox.model.db.entity.Fly;
import com.flybuilder.flybox.model.db.repository.FlyRepo;
import com.flybuilder.flybox.model.dto.response.FlyInfoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FlyServiceImplTest {

    @InjectMocks
    private FlyServiceImpl flyService;

    @Mock
    private FlyRepo flyRepo;

    private static final Long EXISTING_FLY_ID = 1L;
    private static final Long NON_EXISTING_FLY_ID = 2L;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetFlyExistingFly() {
        Fly existingFly = new Fly();
        existingFly.setId(EXISTING_FLY_ID);

        Mockito.when(flyRepo.findById(EXISTING_FLY_ID)).thenReturn(Optional.of(existingFly));

        FlyInfoResponse response = flyService.getFly(EXISTING_FLY_ID);

        assertEquals(EXISTING_FLY_ID, response.getId());
    }

    @Test
    public void testGetFlyNonExistingFly() {
        Mockito.when(flyRepo.findById(NON_EXISTING_FLY_ID)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            flyService.getFly(NON_EXISTING_FLY_ID);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void getAllFlies() {
    }

    @Test
    void createFly() {
    }

    @Test
    void updateFly() {
    }

    @Test
    void deleteFly() {
    }

    @Test
    void convertToFly() {
    }
}