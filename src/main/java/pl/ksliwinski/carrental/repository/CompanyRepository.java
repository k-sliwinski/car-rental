package pl.ksliwinski.carrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ksliwinski.carrental.model.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

}
