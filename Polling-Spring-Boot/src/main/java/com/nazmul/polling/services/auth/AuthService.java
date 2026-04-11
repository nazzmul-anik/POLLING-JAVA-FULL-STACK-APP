package com.nazmul.polling.services.auth;

import com.nazmul.polling.dto.AuthenticationRequest;
import com.nazmul.polling.dto.AuthenticationResponse;
import com.nazmul.polling.dto.SignupRequest;
import com.nazmul.polling.dto.UserDTO;
import com.nazmul.polling.entity.User;

public interface AuthService {
    AuthenticationResponse signUp(SignupRequest signupRequest);
    User createUser(SignupRequest signupRequest);
    Boolean hasUserWithEmail(String email);
    AuthenticationResponse login(AuthenticationRequest  request);
}
