package com.example.ducanh.dto.request;

import com.example.ducanh.exception.ErrorCode;
import com.example.ducanh.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    String id;
    @Size(min = 4, max=20,message = "USERNAME_INVALID")
    String username;

    @Size(min = 6,message = "PASSWORD_INVALID")
    String password;

    String firstName;
    String lastName;

    @DobConstraint(min = 15,message = "INVALID_AGE")
    LocalDate birthDate;


    List<String> roles;

}
