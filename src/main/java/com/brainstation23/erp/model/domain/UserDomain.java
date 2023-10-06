package com.brainstation23.erp.model.domain;

import com.brainstation23.erp.persistence.entity.auth.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDomain {
    private String firstname;
    private String lastname;
    private String email;
    private Role role;
}
