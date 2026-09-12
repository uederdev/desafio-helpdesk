package br.com.techagro.helpdesk.validation;

import br.com.techagro.helpdesk.domain.CategoryTicket;
import br.com.techagro.helpdesk.repository.CategoryRepository;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CategoryDuplicatedValidation implements IModelValidation<CategoryTicket>{

    private final CategoryRepository repository;

    public CategoryDuplicatedValidation(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validate(CategoryTicket model) {
        repository.findByDescription(model.getDescription())
                .ifPresent(x -> {
                    throw new ValidationException("Category already exists");
                });
    }
}
