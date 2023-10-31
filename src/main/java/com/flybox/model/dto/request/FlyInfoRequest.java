package com.flybox.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.flybox.model.enums.FlyType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlyInfoRequest {

    String name;
    FlyType flyType;
    String description;
    String pic;
    String video;

}
