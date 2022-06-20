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
    public void createUser(@PathVariable AuthMethod authMethod, @RequestBody @Valid CreateUserBodyDTO body) {
        userService.createUser(authMethod, body);
    }

    @PostMapping("/{authMethod}/user/{externalId}/assign-token")
    public void assignTokenToUser(@PathVariable AuthMethod authMethod, @PathVariable String externalId) {
        userService.assignTokenToUser(authMethod, externalId);
    }
}
