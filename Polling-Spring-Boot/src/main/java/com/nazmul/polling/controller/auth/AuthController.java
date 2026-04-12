package com.nazmul.polling.controller.auth;

import com.nazmul.polling.dto.AuthenticationRequest;
import com.nazmul.polling.dto.AuthenticationResponse;
import com.nazmul.polling.dto.SignupRequest;
import com.nazmul.polling.services.auth.AuthService;
import com.nazmul.polling.services.jwt.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest){
        AuthenticationResponse response = authService.signUp(signupRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AuthenticationRequest authenticationRequest){
        AuthenticationResponse response = authService.login(authenticationRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
