package com.brainstation23.erp.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestUpdate {
    @NotNull
    private String firstname;
    @NotNull
    private String lastname;
}
