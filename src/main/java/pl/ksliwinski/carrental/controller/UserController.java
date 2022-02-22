package pl.ksliwinski.carrental.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import pl.ksliwinski.carrental.model.User;
import pl.ksliwinski.carrental.model.dto.UserDto;
import pl.ksliwinski.carrental.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping
    public List<UserDto> getAllUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(defaultValue = "ASC") Sort.Direction direction, @RequestParam(defaultValue = "id") String by) {
        return userService.getAll(page, size, direction, by).stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        return modelMapper.map(
                userService.update(id, modelMapper.map(userDto, User.class)),
                UserDto.class);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
    }
}
