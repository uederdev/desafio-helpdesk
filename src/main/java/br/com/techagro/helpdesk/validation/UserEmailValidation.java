package br.com.techagro.helpdesk.validation;

import br.com.techagro.helpdesk.domain.User;
import br.com.techagro.helpdesk.exception.UserDuplicadoException;
import br.com.techagro.helpdesk.repository.UserRepository;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class UserEmailValidation implements IModelValidation<User> {

    private final UserRepository repository;

    public UserEmailValidation(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validate(User model) {
        repository.findByEmail(model.getEmail())
                .filter(existing -> model.getId() == null || !model.getId().equals(existing.getId()))
                .ifPresent(existing -> {
                    throw new UserDuplicadoException("E-mail já cadastrado.");
                });
    }
}
