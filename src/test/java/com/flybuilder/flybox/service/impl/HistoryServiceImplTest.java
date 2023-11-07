package com.flybuilder.flybox.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flybuilder.flybox.exceptions.CustomException;
import com.flybuilder.flybox.model.db.entity.History;
import com.flybuilder.flybox.model.db.repository.HistoryRepo;
import com.flybuilder.flybox.model.dto.request.HistoryInfoRequest;
import com.flybuilder.flybox.model.dto.response.HistoryInfoResponse;
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
class HistoryServiceImplTest {

    @InjectMocks
    private HistoryServiceImpl historyService;

    @Mock
    private Converters converters;

    @Mock
    private HistoryRepo historyRepo;

    @Spy
    private ObjectMapper mapper;

    private static final Long EXISTING_HISTORY_ID = 1L;
    private static final Long NON_EXISTING_HISTORY_ID = 0L;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetHistoryExistingHistory() {
        History existingHistory = new History();
        existingHistory.setId(EXISTING_HISTORY_ID);

        when(historyRepo.findById(EXISTING_HISTORY_ID)).thenReturn(Optional.of(existingHistory));

        HistoryInfoResponse response = historyService.getHistory(EXISTING_HISTORY_ID);

        assertEquals(EXISTING_HISTORY_ID, response.getId());
    }

    @Test
    public void testGetHistoryWhenIdIsZero() {
        long id = 0L;

        try {
            historyService.getHistory(id);
            fail("Expected CustomException, but it was not thrown.");
        } catch (CustomException e) {
            assertEquals("Легенда не найдена", e.getMessage());
            assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        }
    }

    @Test
    public void testGetHistoryNonExistingHistory() {
        when(historyRepo.findById(NON_EXISTING_HISTORY_ID)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            historyService.getHistory(NON_EXISTING_HISTORY_ID);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void testGetAllHistories() {
        History history1 = new History();
        history1.setId(1L);
        History history2 = new History();
        history2.setId(2L);

        List<History> historyList = Arrays.asList(history1, history2);
        when(historyRepo.findAll()).thenReturn(historyList);
        List<HistoryInfoResponse> historyInfoResponses = historyService.getAllHistories();
        verify(historyRepo).findAll();
        assertEquals(2, historyInfoResponses.size());
    }

    @Test
    public void testCreateHistory() {

        HistoryInfoRequest request = new HistoryInfoRequest();
        request.setHook("Test Hook");

        History history = new History();
        history.setId(1L);
        history.setHook(request.getHook());

        when(historyRepo.save(any(History.class))).thenReturn(history);

        HistoryInfoResponse historyInfoResponse = historyService.createHistory(request);

        verify(historyRepo, times(1)).save(any(History.class));
        verify(mapper, times(1)).convertValue(eq(request), eq(History.class));
        assertNotNull(historyInfoResponse);
    }


    @Test
    public void testUpdateHistory() {
        History history = new History();
        history.setId(1L);
        history.setHook("Old Hook");
        history.setTail("Old Tail");
        history.setRibbing("Old Ribbing");
        history.setBody("Old Body");
        history.setHackle("Old Hackle");
        history.setWing("Old Wing");
        history.setLegs("Old Legs");
        history.setHead("Old Head");
        history.setStatus(Status.CREATED);

        HistoryInfoRequest request = new HistoryInfoRequest();
        request.setHook("New Hook");
        request.setTail("New Tail");
        request.setRibbing("New Ribbing");
        request.setBody("New Body");
        request.setHackle("New Hackle");
        request.setWing("New Wing");
        request.setLegs("New Legs");
        request.setHead("New Head");

        when(historyRepo.findById(1L)).thenReturn(Optional.of(history));
        when(mapper.convertValue(history, HistoryInfoResponse.class)).thenReturn(new HistoryInfoResponse());

        HistoryInfoResponse historyInfoResponse = historyService.updateHistory(1L, request);

        verify(historyRepo, times(1)).findById(1L);
        verify(historyRepo, times(1)).save(any(History.class));
        verify(mapper, times(1)).convertValue(history, HistoryInfoResponse.class);


        // Проверяем, что объект обновлен
        assertEquals("New Hook", history.getHook());
        assertEquals("New Tail", history.getTail());
        assertEquals("New Ribbing", history.getRibbing());
        assertEquals("New Body", history.getBody());
        assertEquals("New Hackle", history.getHackle());
        assertEquals("New Wing", history.getWing());
        assertEquals("New Legs", history.getLegs());
        assertEquals("New Head", history.getHead());
        assertEquals(Status.UPDATED, history.getStatus());
    }

    @Test
    public void testUpdateHistoryWhenIdIsZero() {
        long id = 0L;
        HistoryInfoRequest request = new HistoryInfoRequest();

        try {
            historyService.updateHistory(id, request);
            fail("Expected CustomException, but it was not thrown.");
        } catch (CustomException e) {
            assertEquals("Легенда не найдена", e.getMessage());
            assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        }
    }

    @Test
    public void testUpdateHistoryWhenHistoryIsNull() {
        long id = 1L;
        HistoryInfoRequest request = new HistoryInfoRequest();

        when(historyRepo.findById(id)).thenReturn(Optional.empty());

        HistoryInfoResponse response = historyService.updateHistory(id, request);

        assertNull(response);
    }

    @Test
    public void testDeleteHistory() {
        Long historyId = 1L;

        doNothing().when(historyRepo).deleteById(historyId);

        historyService.deleteHistory(historyId);

        verify(historyRepo, times(1)).deleteById(historyId);
    }

    @Test
    public void testConvertToHistory() {
        HistoryInfoRequest historyInfoRequest = new HistoryInfoRequest();
        historyInfoRequest.setHook("Test Hook");
        historyInfoRequest.setTail("Test Tail");
        historyInfoRequest.setRibbing("Test Ribbing");
        historyInfoRequest.setBody("Test Body");
        historyInfoRequest.setHackle("Test Hackle");
        historyInfoRequest.setWing("Test Wing");
        historyInfoRequest.setLegs("Test Legs");
        historyInfoRequest.setHead("Test Head");

        History history = historyService.convertToHistory(historyInfoRequest);

        assertEquals(historyInfoRequest.getHook(), history.getHook());
        assertEquals(historyInfoRequest.getTail(), history.getTail());
        assertEquals(historyInfoRequest.getRibbing(), history.getRibbing());
        assertEquals(historyInfoRequest.getBody(), history.getBody());
        assertEquals(historyInfoRequest.getHackle(), history.getHackle());
        assertEquals(historyInfoRequest.getWing(), history.getWing());
        assertEquals(historyInfoRequest.getLegs(), history.getLegs());
        assertEquals(historyInfoRequest.getHead(), history.getHead());
    }
}
