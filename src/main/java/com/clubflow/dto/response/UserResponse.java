package com.clubflow.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class UserResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String birthDate;
    private String image;
    private String club;
    private String role;
    private List<String> roles;
}