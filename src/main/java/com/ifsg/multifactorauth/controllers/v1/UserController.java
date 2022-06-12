package com.ifsg.multifactorauth.controllers.v1;

import com.ifsg.multifactorauth.entities.UserEntity;
import com.ifsg.multifactorauth.models.dtos.CreateUserBodyDTO;
import com.ifsg.multifactorauth.models.enums.AuthMethod;
import com.ifsg.multifactorauth.services.UserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "api/v1/method")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/{authMethod}/user/{externalId}/details")
    public UserEntity getUserDetails(@PathVariable AuthMethod authMethod, @PathVariable String externalId) {
        return userService.getUserDetails(authMethod, externalId);
    }

    @PostMapping("/{authMethod}/user/create")
    public UserEntity createUser(@PathVariable AuthMethod authMethod, @RequestBody @Valid CreateUserBodyDTO body) {
        return userService.createUser(authMethod, body);
    }
}
