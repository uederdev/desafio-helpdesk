package br.com.techagro.helpdesk.dto.user;

import br.com.techagro.helpdesk.domain.enums.RoleUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequestCreate(
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 3, max = 100)
        String name,
        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,
        @NotNull(message = "Role é obrigatório")
        RoleUser role
) {
}
