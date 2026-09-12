package br.com.techagro.helpdesk.service;

import br.com.techagro.helpdesk.dto.user.UserResponse;
import br.com.techagro.helpdesk.dto.user.UserRequestCreate;
import jakarta.validation.Valid;

import java.util.List;

public interface UserService {

    List<UserResponse> findAll();

    UserResponse save(@Valid UserRequestCreate dados);

    UserResponse findById(Long id);
}
