package com.brainstation23.erp.controller.rest.auth;

import com.brainstation23.erp.mapper.UserMapper;
import com.brainstation23.erp.model.dto.UserResponse;
import com.brainstation23.erp.model.dto.UserResponseUpdate;
import com.brainstation23.erp.model.dto.auth.AuthenticationRequest;
import com.brainstation23.erp.model.dto.auth.AuthenticationResponse;
import com.brainstation23.erp.model.dto.auth.RegisterRequest;
import com.brainstation23.erp.model.dto.UserRequestUpdate;
import com.brainstation23.erp.service.auth.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
         return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }


    @GetMapping(value = "/users")
    public List<UserResponse> getAllUsers(){
        List<UserResponse> users_id = new ArrayList<>();
        users_id = authenticationService.getAllUsers();
        return users_id;
    }

    @Operation(summary = "Get All Users")
    @GetMapping("/get-all-user")
    public ResponseEntity<Page<UserResponse>> getAll(@ParameterObject Pageable pageable) {
        var domains = authenticationService.getAll(pageable);
        return ResponseEntity.ok(domains.map(userMapper::domainToResponse));
    }

    @GetMapping("/get-user/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Integer id){
        return ResponseEntity.ok(authenticationService.get_user(id));
    }


    @PutMapping("/update-user/{id}")
    public ResponseEntity<UserResponseUpdate> updateUser(@RequestBody @Valid UserRequestUpdate request, @PathVariable Integer id){
        return ResponseEntity.ok(authenticationService.update_user(request, id));
    }

}
