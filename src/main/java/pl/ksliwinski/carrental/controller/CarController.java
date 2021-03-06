package pl.ksliwinski.carrental.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import pl.ksliwinski.carrental.model.Car;
import pl.ksliwinski.carrental.model.dto.CarDto;
import pl.ksliwinski.carrental.service.CarService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;
    private final ModelMapper modelMapper;

    @GetMapping
    public List<CarDto> getAllCars(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(defaultValue = "ASC") Sort.Direction direction, @RequestParam(defaultValue = "id") String by) {
        return carService.getAll(page, size, direction, by).stream()
                .map(car -> modelMapper.map(car, CarDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CarDto getCarById(@PathVariable Long id) {
        return modelMapper.map(carService.findById(id), CarDto.class);
    }

    @GetMapping("/company/{id}")
    public List<CarDto> getCarsByCompany(@PathVariable Long id) {
        return carService.getAllByCompany(id).stream()
                .map(car -> modelMapper.map(car, CarDto.class))
                .collect(Collectors.toList());
    }

    @PostMapping("/rent/{id}")
    public CarDto rentCar(@PathVariable Long id, Principal principal) {
        return modelMapper.map(
                carService.rentCar(id, principal.getName()),
                CarDto.class);
    }

    @PostMapping("/return/{id}")
    public CarDto returnCar(@PathVariable Long id, Principal principal) {
        return modelMapper.map(
                carService.returnCar(id, principal.getName()),
                CarDto.class);
    }

    @GetMapping("/rented")
    public List<CarDto> getLoggedUserCars(Principal principal) {
        return carService.getAllByUser(principal.getName()).stream()
                .map(car -> modelMapper.map(car, CarDto.class))
                .collect(Collectors.toList());
    }

    @PostMapping("/admin")
    public CarDto addCar(@RequestBody @Valid CarDto carDto) {
        return modelMapper.map(
                carService.save(modelMapper.map(carDto, Car.class)),
                CarDto.class);
    }

    @PutMapping("/admin/{id}")
    public CarDto updateCar(@PathVariable Long id, @RequestBody @Valid CarDto carDto) {
        return modelMapper.map(
                carService.update(id, modelMapper.map(carDto, Car.class)),
                CarDto.class);
    }

    @DeleteMapping("/admin/{id}")
    public void deleteCar(@PathVariable Long id) {
        carService.deleteById(id);
    }
}
