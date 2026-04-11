package com.nazmul.polling.services.auth;

import com.nazmul.polling.dto.AuthenticationResponse;
import com.nazmul.polling.dto.SignupRequest;
import com.nazmul.polling.dto.UserDTO;
import com.nazmul.polling.entity.User;
import com.nazmul.polling.enums.UserRole;
import com.nazmul.polling.exceptions.UserAlreadyExistException;
import com.nazmul.polling.repository.UserRepository;
import com.nazmul.polling.services.jwt.UserService;
import com.nazmul.polling.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Override
    public AuthenticationResponse signUp(SignupRequest signupRequest) {
        if(hasUserWithEmail(signupRequest.getEmail())) {
            throw new UserAlreadyExistException(signupRequest.getEmail()+" already exists");
        }
        UserDTO createdUser = createUser(signupRequest);
        UserDetails userDetails = userService.userDetailsService().loadUserByUsername(signupRequest.getEmail());
        String jwt = jwtUtil.generateToken(userDetails, createdUser.getId());
        return new AuthenticationResponse(jwt, createdUser.getFirstName() + " " + createdUser.getLastName());
    }

    @Override
    public UserDTO createUser(SignupRequest signupRequest) {
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setUserRole(UserRole.USER);
        user.setPassword(
                new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        User createdUser = userRepository.save(user);
        return createdUser.getUserDTO();
    }

    @Override
    public Boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }
}
