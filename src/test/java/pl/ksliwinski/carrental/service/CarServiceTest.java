package pl.ksliwinski.carrental.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import pl.ksliwinski.carrental.data.TestData;
import pl.ksliwinski.carrental.exception.CarIsNotRentedByThisUserException;
import pl.ksliwinski.carrental.exception.CarIsRentedException;
import pl.ksliwinski.carrental.model.Car;
import pl.ksliwinski.carrental.model.User;
import pl.ksliwinski.carrental.repository.CarRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
class CarServiceTest {
    @Autowired
    private CarService carService;
    @MockBean
    private CarRepository carRepository;
    @MockBean
    private UserService userService;
    @MockBean
    private CompanyService companyService;
    private TestData testData = new TestData();
    private Car car1, car2;
    private User user1, user2;

    @BeforeEach
    void setUp() {
        user1 = testData.getUser(1L);
        user2 = testData.getUser(2L);
        car1 = testData.getCar(1L);
        car2 = testData.getCar(2L);
    }

    @Test
    void shouldReturnAllCars_whenGettingAll() {
        when(carRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(car1, car2)));

        List<Car> cars = carService.getAll(0, 10, Sort.Direction.ASC, "id");
        assertEquals(2, cars.size());
    }

    @Test
    void shouldReturnCar_whenCreatingNewCar() {
        when(carRepository.save(any(Car.class)))
                .thenReturn(car1);

        Car car = carService.save(car1);
        assertAll(
                () -> assertEquals(car1.getBrand(), car.getBrand()),
                () -> assertEquals(car1.getModel(), car.getModel()),
                () -> assertEquals(car1.getYear(), car.getYear()),
                () -> assertEquals(car1.getColor(), car.getColor()),
                () -> assertEquals(car1.getDailyFee(), car.getDailyFee()),
                () -> assertEquals(car1.getRentDate(), car.getRentDate()),
                () -> assertEquals(car1.isAvailable(), car.isAvailable()),
                () -> assertEquals(car1.getCompany(), car.getCompany())
        );
    }

    @Test
    void shouldReturnCar_whenFindingById() {
        when(carRepository.findById(anyLong()))
                .thenReturn(Optional.of(car1));

        Car car = carService.findById(1L);
        assertNotNull(car);
    }

    @Test
    void shouldThrowEntityNotFound_whenFindingByNotExistingId() {
        when(carRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> carService.findById(1L));
    }

    @Test
    void shouldReturnCar_whenUpdatingCar() {
        when(carRepository.save(any(Car.class)))
                .thenReturn(car1);
        when(carRepository.findById(anyLong()))
                .thenReturn(Optional.of(car1));

        Car car = carService.update(1L, car1);
        assertAll(
                () -> assertEquals(car1.getBrand(), car.getBrand()),
                () -> assertEquals(car1.getModel(), car.getModel()),
                () -> assertEquals(car1.getYear(), car.getYear()),
                () -> assertEquals(car1.getColor(), car.getColor()),
                () -> assertEquals(car1.getDailyFee(), car.getDailyFee()),
                () -> assertEquals(car1.getRentDate(), car.getRentDate()),
                () -> assertEquals(car1.isAvailable(), car.isAvailable()),
                () -> assertEquals(car1.getCompany(), car.getCompany())
        );
    }

    @Test
    void shouldDeleteCar_whenDeletingById() {
        when(carRepository.findById(anyLong()))
                .thenReturn(Optional.of(car1));
        doNothing().when(carRepository).deleteById(anyLong());

        assertDoesNotThrow(() -> carService.deleteById(1L));
    }

    @Test
    void shouldThrowCarIsRented_whenDeletingByIdRentedCar() {
        when(carRepository.findById(anyLong()))
                .thenReturn(Optional.of(car1));
        car1.setUser(user1);

        assertThrows(CarIsRentedException.class, () -> carService.deleteById(1L));
    }

    @Test
    void shouldReturnAllCarsByCompanyId_whenGettingAllByCompany() {
        when(carRepository.findAllByCompanyId(anyLong()))
                .thenReturn(new ArrayList<>(List.of(car1, car2)));

        List<Car> cars = carService.getAllByCompany(1L);
        assertEquals(2, cars.size());
    }

    @Test
    void shouldReturnAllCarsByUserId_whenGettingAllByUser() {
        when(userService.findByEmail(anyString()))
                .thenReturn(user1);
        when(carRepository.findAllByUserId(anyLong()))
                .thenReturn(new ArrayList<>(List.of(car1, car2)));

        List<Car> cars = carService.getAllByUser(user1.getEmail());
        assertEquals(2, cars.size());
    }

    @Test
    void shouldReturnCar_whenRentingCar() {
        when(carRepository.findById(anyLong()))
                .thenReturn(Optional.of(car1));

        Car car = carService.rentCar(1L, user1.getEmail());
        assertAll(
                () -> assertEquals(car1.isAvailable(), car.isAvailable()),
                () -> assertEquals(car1.getRentDate(), car.getRentDate()),
                () -> assertEquals(car1.getUser(), car.getUser())
        );
    }

    @Test
    void shouldThrowCarIsRented_whenRentingCarAlreadyRented() {
        when(carRepository.findById(anyLong()))
                .thenReturn(Optional.of(car1));
        car1.setUser(user1);

        assertThrows(CarIsRentedException.class, () -> carService.rentCar(1L, user1.getEmail()));
    }

    @Test
    void shouldReturnCar_whenReturningCar() {
        when(carRepository.findById(anyLong()))
                .thenReturn(Optional.of(car1));
        when(userService.findByEmail(anyString()))
                .thenReturn(user1);
        car1.setUser(user1);
        car1.setAvailable(false);
        car1.setRentDate(LocalDate.now());

        Car car = carService.returnCar(1L, user1.getEmail());
        assertAll(
                () -> assertTrue(car.isAvailable()),
                () -> assertNull(car.getRentDate()),
                () -> assertNull(car.getUser())
        );
    }

    @Test
    void shouldThrowCarIsNotRentedByThisUser_whenReturningNotRentedCar() {
        when(carRepository.findById(anyLong()))
                .thenReturn(Optional.of(car1));

        assertThrows(CarIsNotRentedByThisUserException.class, () -> carService.returnCar(1L, user1.getEmail()));
    }

    @Test
    void shouldThrowCarIsNotRentedByThisUser_whenReturningCarRentedByAnotherUser() {
        when(carRepository.findById(anyLong()))
                .thenReturn(Optional.of(car1));
        when(userService.findByEmail(anyString()))
                .thenReturn(user1);
        car1.setUser(user2);
        car1.setAvailable(false);
        car1.setRentDate(LocalDate.now());

        assertThrows(CarIsNotRentedByThisUserException.class, () -> carService.returnCar(1L, user1.getEmail()));
    }
}