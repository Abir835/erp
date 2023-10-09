package com.brainstation23.erp.service;


import com.brainstation23.erp.model.EmployeeRequest;
import com.brainstation23.erp.model.EmployeeResponse;
import com.brainstation23.erp.persistence.entity.Employee;
import com.brainstation23.erp.persistence.entity.auth.Role;
import com.brainstation23.erp.persistence.entity.auth.User;
import com.brainstation23.erp.persistence.repository.EmployeeRepository;
import com.brainstation23.erp.persistence.repository.auth.UserRepository;
import com.brainstation23.erp.service.auth.AuthenticationService;
import com.brainstation23.erp.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private  static final String  DEFAULT_PASSWORD = "1234";
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(DEFAULT_PASSWORD))
                .role(Role.EMPLOYEE)
                .build();
        var userSaved = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        authenticationService.saveUserTokenBypass(userSaved, jwtToken);

        var employee = Employee
                .builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .balance(request.getBalance())
                .user(userSaved)
                .build();
        employeeRepository.save(employee);
        var subject = "Send user Information";
        var body = "email: "+ DEFAULT_PASSWORD + " password: "+ user.getPassword();
        emailService.SendMail(employee.getEmail(),subject,body);
        return EmployeeResponse
                .builder()
                .firstname(employee.getFirstname())
                .lastname(employee.getLastname())
                .email(employee.getEmail())
                .balance(employee.getBalance())
                .build();
    }
}
