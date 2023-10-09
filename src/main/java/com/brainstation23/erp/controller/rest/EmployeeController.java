package com.brainstation23.erp.controller.rest;

import com.brainstation23.erp.model.EmployeeRequest;
import com.brainstation23.erp.model.EmployeeResponse;
import com.brainstation23.erp.model.EmployeeUpdateRequest;
import com.brainstation23.erp.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
