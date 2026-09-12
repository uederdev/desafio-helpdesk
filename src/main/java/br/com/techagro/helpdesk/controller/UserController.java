package br.com.techagro.helpdesk.controller;

import br.com.techagro.helpdesk.config.Util;
import br.com.techagro.helpdesk.dto.user.UserRequestCreate;
import br.com.techagro.helpdesk.dto.user.UserResponse;
import br.com.techagro.helpdesk.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UserResponse> created(@Valid @RequestBody UserRequestCreate dados){
        UserResponse user = service.save(dados);
        URI uri = Util.getUri("/api/users/{id}", user.id());
        return ResponseEntity.created(uri).body(user);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll(){
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }
}
