package br.com.techagro.helpdesk.service;

import br.com.techagro.helpdesk.domain.CategoryTicket;
import br.com.techagro.helpdesk.dto.category.CategoryRequestCreate;
import br.com.techagro.helpdesk.dto.category.CategoryResponse;
import br.com.techagro.helpdesk.mapper.CategoryMapper;
import br.com.techagro.helpdesk.repository.CategoryRepository;
import br.com.techagro.helpdesk.validation.IModelValidation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;
    private final List<IModelValidation<CategoryTicket>> validations;

    public CategoryServiceImpl(CategoryRepository repository, CategoryMapper mapper, List<IModelValidation<CategoryTicket>> validations) {
        this.repository = repository;
        this.mapper = mapper;
        this.validations = validations;
    }

    @Override
    public List<CategoryResponse> findAll() {
        return repository.findAll()
                .stream().map(mapper::toDto).toList();
    }

    @Transactional
    @Override
    public CategoryResponse create(CategoryRequestCreate dados) {
        CategoryTicket categoryTicket = mapper.toModel(dados);
        validations.forEach(validation -> validation.validate(categoryTicket));
        CategoryTicket newCategoryTicket = repository.save(categoryTicket);
        return mapper.toDto(newCategoryTicket);
    }
}
