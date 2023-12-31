package com.flybuilder.flybox.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.flybuilder.flybox.model.enums.FlyType;
import com.flybuilder.flybox.model.enums.Status;
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
    Status status;

}
