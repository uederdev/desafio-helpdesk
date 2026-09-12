package br.com.techagro.helpdesk.service;

import br.com.techagro.helpdesk.domain.User;
import br.com.techagro.helpdesk.dto.user.UserRequestCreate;
import br.com.techagro.helpdesk.dto.user.UserResponse;
import br.com.techagro.helpdesk.exception.ObjectNotFoundException;
import br.com.techagro.helpdesk.mapper.UserMapper;
import br.com.techagro.helpdesk.repository.UserRepository;
import br.com.techagro.helpdesk.validation.IModelValidation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final List<IModelValidation<User>> validations;

    public UserServiceImpl(UserRepository repository, UserMapper mapper, List<IModelValidation<User>> validations) {
        this.repository = repository;
        this.mapper = mapper;
        this.validations = validations;
    }

    @Override
    public List<UserResponse> findAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    @Transactional
    @Override
    public UserResponse save(UserRequestCreate dados) {
        User user = mapper.toModel(dados);
        validations.forEach(v -> v.validate(user));
        User savedUser = repository.save(user);
        return mapper.toDto(savedUser);
    }

    @Override
    public UserResponse findById(Long id) {
        return repository
                .findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ObjectNotFoundException("Id: " + id));
    }
}
