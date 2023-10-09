package com.brainstation23.erp.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeUpdateRequest {
    @NotNull
    private String firstname;
    @NotNull
    private String lastname;
    @NotNull
    private Double balance;
}
