package pl.ksliwinski.carrental.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.ksliwinski.carrental.model.User;
import pl.ksliwinski.carrental.model.dto.UserDto;
import pl.ksliwinski.carrental.service.AuthenticationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final ModelMapper modelMapper;

    @PostMapping("/register")
    public UserDto register(@RequestBody @Valid UserDto userDto) {
        return modelMapper.map(
                authenticationService.registerUser(modelMapper.map(userDto, User.class)),
                UserDto.class);
    }
}
