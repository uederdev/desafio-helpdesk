package br.com.techagro.helpdesk.service;

import br.com.techagro.helpdesk.dto.category.CategoryRequestCreate;
import br.com.techagro.helpdesk.dto.category.CategoryResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface CategoryService {

    List<CategoryResponse> findAll();

    CategoryResponse create(@Valid CategoryRequestCreate dados);

}
