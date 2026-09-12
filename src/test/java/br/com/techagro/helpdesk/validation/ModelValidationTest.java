package br.com.techagro.helpdesk.validation;

import br.com.techagro.helpdesk.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ModelValidationTest {

    @Test
    void selectsValidationsByModelTypeAndReusesThemAcrossConsumers() {
        try (var context = new AnnotationConfigApplicationContext(ValidationConfig.class)) {
            var first = context.getBean(FirstConsumer.class);
            var second = context.getBean(SecondConsumer.class);
            var user = new User();

            assertEquals(2, first.validations().size());
            assertEquals(first.validations(), second.validations());
            first.validations().forEach(validation -> validation.validate(user));
            assertEquals("Validated", user.getName());
            assertEquals("validated@example.com", user.getEmail());
        }
    }

    record FirstConsumer(List<IModelValidation<User>> validations) { }

    record SecondConsumer(List<IModelValidation<User>> validations) { }

    @Configuration(proxyBeanMethods = false)
    static class ValidationConfig {

        @Bean
        IModelValidation<User> nameValidation() {
            return user -> user.setName("Validated");
        }

        @Bean
        IModelValidation<User> emailValidation() {
            return user -> user.setEmail("validated@example.com");
        }

        @Bean
        IModelValidation<String> textValidation() {
            return text -> { throw new AssertionError("Wrong model type"); };
        }

        @Bean
        FirstConsumer firstConsumer(List<IModelValidation<User>> validations) {
            return new FirstConsumer(validations);
        }

        @Bean
        SecondConsumer secondConsumer(List<IModelValidation<User>> validations) {
            return new SecondConsumer(validations);
        }
    }
}
