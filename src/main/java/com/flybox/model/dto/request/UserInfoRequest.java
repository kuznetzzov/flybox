package com.flybox.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.flybox.model.enums.Gender;
import com.flybox.model.enums.Role;
import com.flybox.model.enums.UserStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

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
    UserStatus status;
    Role role;

}
