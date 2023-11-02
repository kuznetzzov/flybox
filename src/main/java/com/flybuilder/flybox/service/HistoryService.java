package com.flybuilder.flybox.service;

import com.flybuilder.flybox.model.db.entity.History;
import com.flybuilder.flybox.model.dto.request.HistoryInfoRequest;
import com.flybuilder.flybox.model.dto.response.HistoryInfoResponse;

import java.util.List;

public interface HistoryService {

    HistoryInfoResponse getHistory(Long id);

    List<HistoryInfoResponse> getAllHistories();

    HistoryInfoResponse createHistory(HistoryInfoRequest historyInfoRequest);

    HistoryInfoResponse updateHistory(Long id, HistoryInfoRequest historyInfoRequest);

    void deleteHistory(Long id);

    HistoryInfoResponse convertToHistoryInfoResponse(History history);

    History convertToHistory(HistoryInfoRequest request);

}
