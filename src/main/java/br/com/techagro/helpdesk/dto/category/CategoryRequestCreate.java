package br.com.techagro.helpdesk.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequestCreate(
        @NotBlank(message = "Campo descrição é obrigatório")
        @Size(min = 3, max = 80)
        String description
) {
}
