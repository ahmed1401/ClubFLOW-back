package com.clubflow.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MemberDto {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    private String phoneNumber;

    private String image;

    @NotBlank(message = "Club is required")
    private String club;

    @NotBlank(message = "Role is required")
    private String role;

    private String birthDate;

    private String address;

    private String link;
    private String password;
    private String confirmPassword;


    public MemberDto(String ahmed, String hajje, String mail, String number, String image, String techClub, String president, String date, String tunis, String s) {

    }

    public MemberDto() {

    }
}