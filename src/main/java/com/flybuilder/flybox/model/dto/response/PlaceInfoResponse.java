package com.flybuilder.flybox.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.flybuilder.flybox.model.dto.request.PlaceInfoRequest;
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
public class PlaceInfoResponse extends PlaceInfoRequest {

    Long id;
    Set<FlyInfoResponse> relatedFlies;
}
