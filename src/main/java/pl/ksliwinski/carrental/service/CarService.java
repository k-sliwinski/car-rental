package pl.ksliwinski.carrental.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ksliwinski.carrental.exception.CarIsNotRentedByThisUserException;
import pl.ksliwinski.carrental.exception.CarIsRentedException;
import pl.ksliwinski.carrental.model.Car;
import pl.ksliwinski.carrental.model.User;
import pl.ksliwinski.carrental.repository.CarRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final UserService userService;
    private final CompanyService companyService;

    public List<Car> getAll(int page, int size, Sort.Direction direction, String by) {
        return carRepository.findAll(PageRequest.of(page, size, Sort.by(direction, by))).getContent();
    }

    public Car save(Car car) {
        return carRepository.save(car);
    }

    @Transactional
    public Car update(Long id, Car car) {
        Car carToUpdate = findById(id);
        carToUpdate.setBrand(car.getBrand());
        carToUpdate.setModel(car.getModel());
        carToUpdate.setYear(car.getYear());
        carToUpdate.setColor(car.getColor());
        carToUpdate.setDailyFee(car.getDailyFee());
        carToUpdate.setRentDate(car.getRentDate());
        carToUpdate.setAvailable(car.isAvailable());
        carToUpdate.setCompany(car.getCompany());

        return carToUpdate;
    }

    public void deleteById(Long id) {
        Car carToDelete = findById(id);
        if (carToDelete.getUser() != null) {
            throw new CarIsRentedException("Car is rented by another user!");
        }
        carRepository.deleteById(id);
    }

    public List<Car> getAllByCompany(Long companyId) {
        companyService.findById(companyId);
        return carRepository.findAllByCompanyId(companyId);
    }

    public List<Car> getAllByUser(String email) {
        Long userId = userService.findByEmail(email).getId();
        return carRepository.findAllByUserId(userId);
    }

    @Transactional
    public Car rentCar(Long id, String email) {
        User user = userService.findByEmail(email);
        Car carToRent = findById(id);
        if (carToRent.getUser() != null) {
            throw new CarIsRentedException("Car is already rented!");
        }
        carToRent.setRentDate(LocalDate.now());
        carToRent.setAvailable(false);
        carToRent.setUser(user);

        return carToRent;
    }

    @Transactional
    public Car returnCar(Long id, String email) {
        User user = userService.findByEmail(email);
        Car carToReturn = findById(id);
        if (carToReturn.isAvailable() || !Objects.equals(carToReturn.getUser().getId(), user.getId())) {
            throw new CarIsNotRentedByThisUserException("You are not renting this car!");
        }
        carToReturn.setRentDate(null);
        carToReturn.setAvailable(true);
        carToReturn.setUser(null);

        return carToReturn;
    }

    public Car findById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Car with provided id: " + id + " doesn't exist"));
    }

}
