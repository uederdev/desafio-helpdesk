package br.com.techagro.helpdesk.mapper;

import br.com.techagro.helpdesk.domain.User;
import br.com.techagro.helpdesk.dto.user.UserRequestCreate;
import br.com.techagro.helpdesk.dto.user.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toDto(User model);

    User toModel(UserRequestCreate dados);
}
