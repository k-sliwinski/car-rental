package pl.ksliwinski.carrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ksliwinski.carrental.model.Car;

public interface CarRepository extends JpaRepository<Car, Long> {

}
