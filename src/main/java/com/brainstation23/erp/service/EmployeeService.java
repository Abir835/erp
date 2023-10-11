package com.brainstation23.erp.service;


import com.brainstation23.erp.exception.custom.custom.AlreadyExistsException;
import com.brainstation23.erp.exception.custom.custom.NotFoundException;
import com.brainstation23.erp.model.EmployeeRequest;
import com.brainstation23.erp.model.EmployeeResponse;
import com.brainstation23.erp.model.EmployeeUpdateRequest;
import com.brainstation23.erp.persistence.entity.Employee;
import com.brainstation23.erp.persistence.entity.auth.Role;
import com.brainstation23.erp.persistence.entity.auth.User;
import com.brainstation23.erp.persistence.repository.EmployeeRepository;
import com.brainstation23.erp.persistence.repository.auth.UserRepository;
import com.brainstation23.erp.service.auth.AuthenticationService;
import com.brainstation23.erp.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
        var  userEmailAlreadyExists = userRepository.findByEmail(request.getEmail()).orElse(null);
        if(userEmailAlreadyExists != null){
            throw new AlreadyExistsException("Already Exists Email");
        }
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
        var body = "email: "+ employee.getEmail() + " password: "+ DEFAULT_PASSWORD;
        emailService.SendMail(employee.getEmail(),subject,body);
        return EmployeeResponse
                .builder()
                .firstname(employee.getFirstname())
                .lastname(employee.getLastname())
                .email(employee.getEmail())
                .balance(employee.getBalance())
                .build();
    }

    public EmployeeResponse getEmployee(Integer id) {
        var employee = employeeRepository.findById(id).orElse(null);
//        assert employee != null;
        if (employee == null){
            throw new NotFoundException("Employee Not Found");
        }
        return EmployeeResponse
                .builder()
                .firstname(employee.getFirstname())
                .lastname(employee.getLastname())
                .email(employee.getEmail())
                .balance(employee.getBalance())
                .build();
    }

    public EmployeeResponse updateEmployee(Integer id, EmployeeUpdateRequest request) {
        var employee = employeeRepository.findById(id).orElse(null);
//        assert employee != null;
        if (employee == null){
            throw new NotFoundException("Employee Not Found");
        }
        var user = userRepository.findById(employee.getUser().getId()).orElse(null);
        if (user == null){
            throw new NotFoundException("User Not Found");
        }
//        assert user != null;
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        userRepository.save(user);

        employee.setFirstname(request.getFirstname());
        employee.setLastname(request.getLastname());
        employee.setBalance(request.getBalance());
        employeeRepository.save(employee);

        return EmployeeResponse
                .builder()
                .firstname(employee.getFirstname())
                .lastname(employee.getLastname())
                .email(employee.getEmail())
                .balance(employee.getBalance())
                .build();
    }

    public String delete(Integer id) {
        var employee = employeeRepository.findById(id).orElse(null);
//        assert employee != null;
        if (employee == null){
            throw new NotFoundException("Employee Not Found");
        }
        var user = userRepository.findById(employee.getUser().getId()).orElse(null);
//        assert user != null;
        if (user == null){
            throw new NotFoundException("Employee Not Found");
        }
        userRepository.deleteById(user.getId());
        employeeRepository.deleteById(employee.getId());
        return "Delete Successfully";
    }

    public EmployeeResponse getEmployeeCurrent(Integer id, String userName) {
        var current_user = userRepository.findByEmail(userName).orElse(null);
        var employee = employeeRepository.findById(id).orElse(null);
//        assert employee != null;
        if (employee == null){
            throw new NotFoundException("Employee Not Found");
        }
        var getUser = userRepository.findById(employee.getUser().getId()).orElse(null);

//        assert current_user != null;
        if (current_user == null){
            throw new NotFoundException("User Not Found");
        }
//        assert getUser != null;
        if (getUser == null){
            throw new NotFoundException("User Not Found");
        }
        if (current_user.getId().equals(getUser.getId()) && getUser.getRole().equals(Role.EMPLOYEE)){
            return EmployeeResponse
                    .builder()
                    .firstname(current_user.getFirstname())
                    .lastname(current_user.getLastname())
                    .email(current_user.getEmail())
                    .balance(getUser.getEmployee().getBalance())
                    .build();
        }
        else{
            throw new NotFoundException("Only see own data");
//           return EmployeeResponse
//                   .builder()
//                   .build();
        }
    }

    public EmployeeResponse updateEmployeeCurrent(Integer id, String userName, EmployeeUpdateRequest request) {
        var current_user = userRepository.findByEmail(userName).orElse(null);
        var employee = employeeRepository.findById(id).orElse(null);
//        assert employee != null;
        if (employee == null){
            throw new NotFoundException("User Not Found");
        }
        var getUser = userRepository.findById(employee.getUser().getId()).orElse(null);

//        assert current_user != null;
        if (current_user == null){
            throw new NotFoundException("User Not Found");
        }
//        assert getUser != null;
        if (getUser == null){
            throw new NotFoundException("User Not Found");
        }
        if (current_user.getId().equals(getUser.getId()) && getUser.getRole().equals(Role.EMPLOYEE)){
            getUser.setFirstname(request.getFirstname());
            getUser.setLastname(request.getLastname());
            userRepository.save(getUser);

            getUser.getEmployee().setFirstname(request.getFirstname());
            getUser.getEmployee().setLastname(request.getLastname());
            employeeRepository.save(getUser.getEmployee());

            return EmployeeResponse
                    .builder()
                    .firstname(current_user.getFirstname())
                    .lastname(current_user.getLastname())
                    .email(current_user.getEmail())
                    .balance(getUser.getEmployee().getBalance())
                    .build();
        }
        else{
            throw new NotFoundException("Only update own data");
//            return EmployeeResponse
//                    .builder()
//                    .build();
        }
    }
}
