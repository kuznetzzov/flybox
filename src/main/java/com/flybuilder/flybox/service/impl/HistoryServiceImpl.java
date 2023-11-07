package com.flybuilder.flybox.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flybuilder.flybox.exceptions.CustomException;
import com.flybuilder.flybox.model.db.entity.*;
import com.flybuilder.flybox.model.db.repository.HistoryRepo;
import com.flybuilder.flybox.model.dto.request.*;
import com.flybuilder.flybox.model.dto.response.*;
import com.flybuilder.flybox.model.enums.Status;
import com.flybuilder.flybox.service.HistoryService;
import com.flybuilder.flybox.utils.Converters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepo historyRepo;
    private final ObjectMapper mapper;
    private final Converters converters;

    private final String errNotFound = "Легенда не найдена";

    @Override
    public HistoryInfoResponse getHistory(Long id) {

        HistoryInfoResponse response;

        if (id != 0L) {
            History history = historyRepo.findById(id).orElse(new History());
            response = mapper.convertValue(history, HistoryInfoResponse.class);
        } else {
            throw new CustomException(errNotFound, HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @Override
    public List<HistoryInfoResponse> getAllHistories() {
        List<History> histories = historyRepo.findAll();
        return histories.stream().map(converters::convertToHistoryInfoResponse).collect(Collectors.toList());
    }

    @Override
    public HistoryInfoResponse createHistory(HistoryInfoRequest request) {
        History history = mapper.convertValue(request, History.class);
        history.setCreatedAt(LocalDateTime.now());
        history.setStatus(Status.CREATED);
        History save = historyRepo.save(history);
        return mapper.convertValue(save, HistoryInfoResponse.class);
    }

    @Override
    public HistoryInfoResponse updateHistory(Long id, HistoryInfoRequest request) {

        if (id == 0L) {
            throw new CustomException(errNotFound, HttpStatus.NOT_FOUND);
        }

        History history = historyRepo.findById(id).orElse(null);
        if (history == null) {
            return null;
        }

        history.setHook(StringUtils.isBlank(request.getHook()) ? history.getHook() : request.getHook());
        history.setTail(StringUtils.isBlank(request.getTail()) ? history.getTail() : request.getTail());
        history.setRibbing(StringUtils.isBlank(request.getRibbing()) ? history.getRibbing() : request.getRibbing());
        history.setBody(StringUtils.isBlank(request.getBody()) ? history.getBody() : request.getBody());
        history.setHackle(StringUtils.isBlank(request.getHackle()) ? history.getHackle() : request.getHackle());
        history.setWing(StringUtils.isBlank(request.getWing()) ? history.getWing() : request.getWing());
        history.setLegs(StringUtils.isBlank(request.getLegs()) ? history.getLegs() : request.getLegs());
        history.setHead(StringUtils.isBlank(request.getHead()) ? history.getHead() : request.getHead());

        history.setStatus(Status.UPDATED);
        history.setUpdatedAt(LocalDateTime.now());

        History save = historyRepo.save(history);
        return mapper.convertValue(save, HistoryInfoResponse.class);
    }

    @Override
    public void deleteHistory(Long id) {
        historyRepo.deleteById(id);
    }

    public History convertToHistory(HistoryInfoRequest request) {
        History history = new History();
        history.setHook(request.getHook());
        history.setTail(request.getTail());
        history.setRibbing(request.getRibbing());
        history.setBody(request.getBody());
        history.setHackle(request.getHackle());
        history.setWing(request.getWing());
        history.setLegs(request.getLegs());
        history.setHead(request.getHead());
        return history;
    }

}
