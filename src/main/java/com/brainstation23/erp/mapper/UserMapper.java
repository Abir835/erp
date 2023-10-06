package com.brainstation23.erp.mapper;


import com.brainstation23.erp.model.domain.UserDomain;
import com.brainstation23.erp.model.dto.UserResponse;
import com.brainstation23.erp.persistence.entity.auth.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDomain entityToDomain(User entity);

    UserResponse domainToResponse(UserDomain user);
}
