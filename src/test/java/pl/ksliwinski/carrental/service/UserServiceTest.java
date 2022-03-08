package pl.ksliwinski.carrental.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import pl.ksliwinski.carrental.exception.EmailAlreadyTakenException;
import pl.ksliwinski.carrental.exception.UserIsStillRentingCarsException;
import pl.ksliwinski.carrental.model.Car;
import pl.ksliwinski.carrental.model.User;
import pl.ksliwinski.carrental.repository.CarRepository;
import pl.ksliwinski.carrental.repository.UserRepository;
import pl.ksliwinski.carrental.service.data.TestData;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CarRepository carRepository;
    private TestData testData = new TestData();
    private User user1, user2;
    private Car car;

    @BeforeEach
    void setUp() {
        user1 = testData.getUser(1L);
        user2 = testData.getUser(2L);
        car = testData.getCar(1L);
    }

    @Test
    void shouldReturnUser_whenFindingByEmail() {
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(user1));

        User user = userService.findByEmail(anyString());
        assertNotNull(user);
    }

    @Test
    void shouldThrowEntityNotFound_whenFindingByNotExistingEmail() {
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.findByEmail(anyString()));
    }

    @Test
    void shouldReturnAllUsers_whenGettingAll() {
        when(userRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(user1, user2)));

        List<User> users = userService.getAll(0, 10, Sort.Direction.ASC, "id");
        assertEquals(2, users.size());
    }

    @Test
    void shouldDeleteUser_whenDeletingById() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user1));
        doNothing().when(userRepository).deleteById(anyLong());

        assertDoesNotThrow(() -> userService.deleteById(1L));
    }

    @Test
    void shouldThrowUserIsStillRentingCars_whenDeletingByIdUserWhoIsRentingCars() {
        when(carRepository.findAllByUserId(anyLong()))
                .thenReturn(new ArrayList<>(List.of(car)));

        assertThrows(UserIsStillRentingCarsException.class, () -> userService.deleteById(anyLong()));
    }

    @Test
    void shouldReturnUser_whenFindingById() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user1));

        User user = userService.findById(anyLong());
        assertNotNull(user);
    }

    @Test
    void shouldThrowEntityNotFound_whenFindingByNotExistingId() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.findById(anyLong()));
    }

    @Test
    void shouldReturnUser_whenUpdatingUser() {
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user1));

        User user = userService.update(anyLong(), user1);
        assertAll(
                () -> assertEquals(user1.getFirstName(), user.getFirstName()),
                () -> assertEquals(user1.getLastName(), user.getLastName()),
                () -> assertEquals(user1.getEmail(), user.getEmail())
        );
    }

    @Test
    void shouldThrowEmailAlreadyTaken_whenUpdatingUserWithExistingEmail() {
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(user2));

        assertThrows(EmailAlreadyTakenException.class, () -> userService.update(anyLong(), user1));
    }
}
