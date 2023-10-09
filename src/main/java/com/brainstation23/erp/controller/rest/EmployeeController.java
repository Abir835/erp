package com.brainstation23.erp.controller.rest;

import com.brainstation23.erp.model.EmployeeRequest;
import com.brainstation23.erp.model.EmployeeResponse;
import com.brainstation23.erp.model.EmployeeUpdateRequest;
import com.brainstation23.erp.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Null;
import javax.validation.constraints.Past;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EmployeeController {
    private final EmployeeService employeeService;
    @PostMapping("/employee")
    public ResponseEntity<EmployeeResponse> createEmployee(@RequestBody @Valid EmployeeRequest request){
        return ResponseEntity.ok(employeeService.createEmployee(request));
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<EmployeeResponse> getEmployee(@PathVariable Integer id){
        return ResponseEntity.ok(employeeService.getEmployee(id));
    }

    @PutMapping("/employee/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(@PathVariable Integer id, @RequestBody EmployeeUpdateRequest request){
        return ResponseEntity.ok(employeeService.updateEmployee(id, request));
    }

    @DeleteMapping("/employee/{id}")
    public String delete(@PathVariable Integer id){
        return employeeService.delete(id);
    }

    @GetMapping("/employee/get/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeCurrent(@PathVariable Integer id, Authentication authentication){
        var currentUserName = authentication.getName();
        return ResponseEntity.ok(employeeService.getEmployeeCurrent(id, currentUserName));
    }

    @PutMapping("/employee/get/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployeeCurrent(@PathVariable Integer id, Authentication authentication, @RequestBody EmployeeUpdateRequest request){
        var userName = authentication.getName();
        return ResponseEntity.ok(employeeService.updateEmployeeCurrent(id, userName, request));
    }

}
