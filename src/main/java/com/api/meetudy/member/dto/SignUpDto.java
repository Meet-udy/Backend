package com.api.meetudy.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignUpDto {

    @NotBlank
    @Email(message = "Invalid email format.")
    private String email;

    @NotBlank
    @Pattern(
            regexp = "^[a-zA-Z0-9]{5,15}$",
            message = "Username must be 5 to 15 characters long and consist of only letters and numbers."
    )
    private String username;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,17}$",
            message = "Password must be 8 to 17 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character."
    )
    private String password;

    @NotBlank
    private String nickname;

}