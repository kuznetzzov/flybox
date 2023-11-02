package com.flybuilder.flybox.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.flybuilder.flybox.model.dto.request.FlyInfoRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlyInfoResponse extends FlyInfoRequest {

    Long id;

    Set<HistoryInfoResponse> relatedHistories;
    Set<MaterialInfoResponse> relatedMaterials;
    Set<PlaceInfoResponse> relatedPlaces;
    Set<UserInfoResponse> relatedUsers;
}
