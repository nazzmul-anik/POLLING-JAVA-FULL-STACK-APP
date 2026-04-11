package com.nazmul.polling.services.auth;

import com.nazmul.polling.dto.AuthenticationResponse;
import com.nazmul.polling.dto.SignupRequest;
import com.nazmul.polling.dto.UserDTO;

public interface AuthService {
    AuthenticationResponse signUp(SignupRequest signupRequest);
    UserDTO createUser(SignupRequest signupRequest);
    Boolean hasUserWithEmail(String email);
}
