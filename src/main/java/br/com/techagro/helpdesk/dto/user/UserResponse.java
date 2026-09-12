package br.com.techagro.helpdesk.dto.user;

import br.com.techagro.helpdesk.domain.enums.RoleUser;

public record UserResponse(
        Long id,
        String name,
        String email,
        RoleUser role
) {
}
