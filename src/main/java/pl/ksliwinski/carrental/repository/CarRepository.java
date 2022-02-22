package pl.ksliwinski.carrental.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.ksliwinski.carrental.model.Car;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    Page<Car> findAll(Pageable pageable);

    List<Car> findAllByUserId(Long userId);

    List<Car> findAllByCompanyId(Long companyId);
}
