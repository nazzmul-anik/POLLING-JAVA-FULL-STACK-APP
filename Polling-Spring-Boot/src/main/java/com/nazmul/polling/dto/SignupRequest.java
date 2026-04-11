package com.nazmul.polling.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
