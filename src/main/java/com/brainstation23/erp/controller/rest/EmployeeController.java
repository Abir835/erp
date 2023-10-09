package com.brainstation23.erp.controller.rest;

import com.brainstation23.erp.model.EmployeeRequest;
import com.brainstation23.erp.model.EmployeeResponse;
import com.brainstation23.erp.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EmployeeController {
    private final EmployeeService employeeService;
    @PostMapping("/employee")
    public ResponseEntity<EmployeeResponse> createEmployee(@RequestBody @Valid EmployeeRequest request){
        return ResponseEntity.ok(employeeService.createEmployee(request));
    }
}
