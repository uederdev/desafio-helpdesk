package br.com.techagro.helpdesk.mapper;

import br.com.techagro.helpdesk.domain.CategoryTicket;
import br.com.techagro.helpdesk.dto.category.CategoryRequestCreate;
import br.com.techagro.helpdesk.dto.category.CategoryResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse toDto(CategoryTicket model);

    CategoryTicket toModel(CategoryRequestCreate dados);
}
