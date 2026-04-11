package com.nazmul.polling.services.auth;

import com.nazmul.polling.dto.AuthenticationRequest;
import com.nazmul.polling.dto.AuthenticationResponse;
import com.nazmul.polling.dto.SignupRequest;
import com.nazmul.polling.entity.User;
import com.nazmul.polling.enums.UserRole;
import com.nazmul.polling.exceptions.UserAlreadyExistException;
import com.nazmul.polling.exceptions.UserNotFoundException;
import com.nazmul.polling.repository.UserRepository;
import com.nazmul.polling.services.jwt.UserService;
import com.nazmul.polling.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse signUp(SignupRequest signupRequest) {
        if(hasUserWithEmail(signupRequest.getEmail())) {
            throw new UserAlreadyExistException(signupRequest.getEmail()+" already exists");
        }
        User createdUser = createUser(signupRequest);
        String jwt = jwtUtil.generateToken(createdUser, createdUser.getId());
        return new AuthenticationResponse(jwt, createdUser.getFirstName() + " " + createdUser.getLastName());
    }

    @Override
    public User createUser(SignupRequest signupRequest) {
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setUserRole(UserRole.USER);
        user.setPassword(
                new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        Optional<User> optionalUser = userRepository.findFirstByEmail(request.getEmail());
        if(optionalUser.isEmpty()){
            throw new UserNotFoundException(request.getEmail() + " not found");
        }
        User user = optionalUser.get();
        String jwt = jwtUtil.generateToken(user, user.getId());
        return new AuthenticationResponse(jwt, user.getFirstName() + " " + user.getLastName());
    }
}
