package com.flybuilder.flybox.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.flybuilder.flybox.model.enums.Gender;
import com.flybuilder.flybox.model.enums.Role;
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
public class UserInfoRequest {

    String email;
    String password;
    String username;
    Integer age;
    Gender gender;
    Status status;
    Role role;

}
