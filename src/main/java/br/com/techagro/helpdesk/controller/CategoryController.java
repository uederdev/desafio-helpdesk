package br.com.techagro.helpdesk.controller;

import br.com.techagro.helpdesk.config.Util;
import br.com.techagro.helpdesk.dto.category.CategoryRequestCreate;
import br.com.techagro.helpdesk.dto.category.CategoryResponse;
import br.com.techagro.helpdesk.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findAll(){
        return ResponseEntity.ok().body(service.findAll());
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequestCreate dados){
        CategoryResponse response = service.create(dados);
        URI uri = Util.getUri("/api/categories/{id}", response.id());
        return ResponseEntity.created(uri).body(response);
    }
}
