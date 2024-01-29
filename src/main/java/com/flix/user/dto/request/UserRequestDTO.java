package com.flix.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDTO {
    @JsonProperty("name")
    @NotBlank(message = "name is mandatory")
    @Size(min = 5, message = "name must have at least 5 characters")
    private String name;

    @JsonProperty("email")
    @NotBlank(message = "email is mandatory")
    @Email(message = "email must be a valid email address")
    private String email;
}
