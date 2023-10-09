package com.brainstation23.erp.model.dto;


import com.brainstation23.erp.persistence.entity.auth.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseUpdate {
    private String firstname;
    private String lastname;
    private Role role;
    private String email;
}
