package com.consultationapp.demo.dto;

import com.consultationapp.demo.entity.UserRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {

    private String firstName;
    private String lastName;
    private String email;

    private String phone;

    private String password;
    private UserRole role;
}