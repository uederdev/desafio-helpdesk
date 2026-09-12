package br.com.techagro.helpdesk.validation;

import br.com.techagro.helpdesk.domain.User;
import br.com.techagro.helpdesk.exception.UserDuplicadoException;
import br.com.techagro.helpdesk.repository.UserRepository;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserEmailValidationTest {

    private final UserRepository repository = mock(UserRepository.class);
    private final UserEmailValidation validation = new UserEmailValidation(repository);

    @Test
    void acceptsUnusedEmail() {
        User user = user(null);
        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> validation.validate(user));
    }

    @Test
    void rejectsDuplicateEmailOnCreation() {
        User user = user(null);
        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user(1L)));
        assertThrows(UserDuplicadoException.class, () -> validation.validate(user));
    }

    @Test
    void acceptsOwnEmailOnUpdate() {
        User user = user(1L);
        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user(1L)));
        assertDoesNotThrow(() -> validation.validate(user));
    }

    @Test
    void rejectsAnotherUsersEmailOnUpdate() {
        User user = user(2L);
        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user(1L)));
        assertThrows(UserDuplicadoException.class, () -> validation.validate(user));
    }

    private User user(Long id) {
        User user = new User();
        user.setId(id);
        user.setEmail("user@example.com");
        return user;
    }
}
