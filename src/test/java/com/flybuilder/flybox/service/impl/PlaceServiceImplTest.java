package com.flybuilder.flybox.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flybuilder.flybox.exceptions.CustomException;
import com.flybuilder.flybox.model.db.entity.Place;
import com.flybuilder.flybox.model.db.repository.PlaceRepo;
import com.flybuilder.flybox.model.dto.request.PlaceInfoRequest;
import com.flybuilder.flybox.model.dto.response.PlaceInfoResponse;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class PlaceServiceImplTest {

    @InjectMocks
    private PlaceServiceImpl placeService;

    @Mock
    private Converters converters;

    @Mock
    private PlaceRepo placeRepo;

    @Spy
    private ObjectMapper mapper;

    private static final Long EXISTING_ID = 1L;
    private static final Long NON_EXISTING_ID = 0L;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetPlaceExistingPlace() {
        Place existingPlace = new Place();
        existingPlace.setId(EXISTING_ID);

        when(placeRepo.findById(EXISTING_ID)).thenReturn(Optional.of(existingPlace));

        PlaceInfoResponse response = placeService.getPlace(EXISTING_ID);

        assertEquals(EXISTING_ID, response.getId());
    }

    @Test
    public void testGetPlaceWhenIdIsZero() {
        long id = 0L;

        try {
            placeService.getPlace(id);
            fail("Expected CustomException, but it was not thrown.");
        } catch (CustomException e) {
            assertEquals("Водоём не найден", e.getMessage());
            assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        }
    }

    @Test
    public void testGetPlaceNonExistingPlace() {
        when(placeRepo.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            placeService.getPlace(NON_EXISTING_ID);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void testGetAllPlaces() {
        Place place1 = new Place();
        place1.setId(1L);
        Place place2 = new Place();
        place2.setId(2L);

        List<Place> placeList = Arrays.asList(place1, place2);
        when(placeRepo.findAll()).thenReturn(placeList);
        List<PlaceInfoResponse> placeInfoResponses = placeService.getAllPlaces();
        verify(placeRepo).findAll();
        assertEquals(2, placeInfoResponses.size());
    }

    @Test
    public void testCreatePlace() {

        PlaceInfoRequest request = new PlaceInfoRequest();
        request.setName("Ладожское озеро");

        Place place = new Place();
        place.setId(1L);
        place.setName(request.getName());

        when(placeRepo.save(any(Place.class))).thenReturn(place);

        PlaceInfoResponse placeInfoResponse = placeService.createPlace(request);

        verify(placeRepo, times(1)).save(any(Place.class));
        verify(mapper, times(1)).convertValue(eq(request), eq(Place.class));
        assertNotNull(placeInfoResponse);
    }


    @Test
    public void testUpdatePlace() {
        Place place = new Place();
        place.setId(1L);
        place.setName("Old Place");
        place.setCoordinates("Old Coordinates");
        place.setFlySinking(0.0F);
        place.setIsSalt(false);
        place.setStatus(Status.CREATED);

        PlaceInfoRequest request = new PlaceInfoRequest();
        request.setName("New Place");
        request.setCoordinates("New Coordinates");
        request.setFlySinking(0.0F);
        request.setIsSalt(true);

        when(placeRepo.findById(1L)).thenReturn(Optional.of(place));
        when(mapper.convertValue(place, PlaceInfoResponse.class)).thenReturn(new PlaceInfoResponse());

        PlaceInfoResponse placeInfoResponse = placeService.updatePlace(1L, request);

        verify(placeRepo, times(1)).findById(1L);
        verify(placeRepo, times(1)).save(any(Place.class));
        verify(mapper, times(1)).convertValue(place, PlaceInfoResponse.class);

        // Проверяем, что объект обновлен
        assertEquals("New Place", place.getName());
        assertEquals("New Coordinates", place.getCoordinates());
        assertEquals(0.0F, place.getFlySinking());
        assertEquals(true, place.getIsSalt());
        assertEquals(Status.UPDATED, place.getStatus());
    }

    @Test
    public void testUpdatePlaceWhenIdIsZero() {
        long id = 0L;
        PlaceInfoRequest request = new PlaceInfoRequest();

        try {
            placeService.updatePlace(id, request);
            fail("Expected CustomException, but it was not thrown.");
        } catch (CustomException e) {
            assertEquals("Водоём не найден", e.getMessage());
            assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        }
    }

    @Test
    public void testUpdatePlaceWhenPlaceIsNull() {
        long id = 1L;
        PlaceInfoRequest request = new PlaceInfoRequest();

        when(placeRepo.findById(id)).thenReturn(Optional.empty());

        PlaceInfoResponse response = placeService.updatePlace(id, request);

        assertNull(response);
    }

    @Test
    public void testDeletePlace() {
        Long placeId = 1L;

        doNothing().when(placeRepo).deleteById(placeId);

        placeService.deletePlace(placeId);

        verify(placeRepo, times(1)).deleteById(placeId);
    }

    @Test
    public void testConvertToPlace() {
        PlaceInfoRequest placeInfoRequest = new PlaceInfoRequest();
        placeInfoRequest.setName("Test Place");
        placeInfoRequest.setCoordinates("Test Coordinates");
        placeInfoRequest.setFlySinking(0.0F);
        placeInfoRequest.setIsSalt(false);

        Place place = placeService.convertToPlace(placeInfoRequest);

        assertEquals(placeInfoRequest.getName(), place.getName());
        assertEquals(placeInfoRequest.getCoordinates(), place.getCoordinates());
        assertEquals(placeInfoRequest.getFlySinking(), place.getFlySinking());
        assertEquals(placeInfoRequest.getIsSalt(), place.getIsSalt());
    }
}
