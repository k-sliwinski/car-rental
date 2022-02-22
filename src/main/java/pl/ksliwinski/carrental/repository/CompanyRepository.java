package pl.ksliwinski.carrental.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.ksliwinski.carrental.model.Company;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    Page<Company> findAll(Pageable pageable);

    List<Company> findByCountryIgnoreCase(String country);
}
