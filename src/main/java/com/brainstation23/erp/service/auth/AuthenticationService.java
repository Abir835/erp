package com.brainstation23.erp.service.auth;

import com.brainstation23.erp.exception.custom.custom.AlreadyExistsException;
import com.brainstation23.erp.exception.custom.custom.NotFoundException;
import com.brainstation23.erp.mapper.UserMapper;
import com.brainstation23.erp.model.domain.UserDomain;
import com.brainstation23.erp.model.dto.UserResponse;
import com.brainstation23.erp.model.dto.UserResponseUpdate;
import com.brainstation23.erp.model.dto.auth.AuthenticationRequest;
import com.brainstation23.erp.model.dto.auth.AuthenticationResponse;
import com.brainstation23.erp.model.dto.auth.RegisterRequest;
import com.brainstation23.erp.model.dto.UserRequestUpdate;
import com.brainstation23.erp.persistence.entity.auth.User;
import com.brainstation23.erp.persistence.entity.token.Tokens;
import com.brainstation23.erp.persistence.entity.token.TokenType;
import com.brainstation23.erp.persistence.repository.auth.UserRepository;
import com.brainstation23.erp.persistence.repository.token.TokenRepository;
import com.brainstation23.erp.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    public AuthenticationResponse register(RegisterRequest request){
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        var  userEmailAlreadyExists = repository.findByEmail(request.getEmail()).orElse(null);
        if(userEmailAlreadyExists != null){
            throw new AlreadyExistsException("Already Exists Email");
        }
        var userSaved = repository.save(user);
        var jwtToken = jwtService.generateToken(user);

        saveUserToken(userSaved, jwtToken);

        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .username(user.getUsername())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .username(user.getUsername())
                .build();
    }

    private void revokeAllUserTokens(User user){
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;

        validUserTokens.forEach(t ->{
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void saveUserTokenBypass(User user, String jwtToken){
        saveUserToken(user, jwtToken);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Tokens.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }


    public List<UserResponse> getAllUsers() {
        return repository.getAllUser();
    }

    public Page<UserDomain> getAll(Pageable pageable) {
        var entities = repository.findAll(pageable);
        return entities.map(userMapper::entityToDomain);
    }

    public UserResponse get_user(Integer id) {
        var user = repository.findById(id).orElse(null);
//        assert user != null;
        if (user == null){
            throw new NotFoundException("User Not Found");
        }
        return UserResponse
                .builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .role(user.getRole())
                .email(user.getEmail())
                .build();
    }

    public UserResponseUpdate update_user(UserRequestUpdate request, Integer id){
        var user = repository.findById(id).orElse(null);
//        assert user != null;
        if (user == null){
            throw new NotFoundException("User Not Found");
        }
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        repository.save(user);

        return UserResponseUpdate.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .role(user.getRole())
                .email(user.getEmail())
                .build();
    }
}

