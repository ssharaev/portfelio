package io.portfel.web;

import io.portfel.web.dto.UserDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @GetMapping
    public UserDto getUser() {
        return new UserDto("1", "name", "email");
    }
}
