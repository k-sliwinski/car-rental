package pl.ksliwinski.carrental.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ksliwinski.carrental.exception.CarIsRentedException;
import pl.ksliwinski.carrental.model.Car;
import pl.ksliwinski.carrental.model.Company;
import pl.ksliwinski.carrental.repository.CarRepository;
import pl.ksliwinski.carrental.repository.CompanyRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CarRepository carRepository;

    public List<Company> getAll(int page, int size, Sort.Direction direction, String by) {
        return companyRepository.findAll(PageRequest.of(page, size, Sort.by(direction, by))).getContent();
    }

    public List<Company> getAllByCountry(String country) {
        return companyRepository.findByCountryIgnoreCase(country);
    }

    public Company save(Company company) {
        return companyRepository.save(company);
    }

    @Transactional
    public Company update(Long id, Company company) {
        Company companyToUpdate = findById(id);
        companyToUpdate.setName(company.getName());
        companyToUpdate.setCountry(company.getCountry());
        companyToUpdate.setCity(company.getCity());
        companyToUpdate.setStreetAddress(company.getStreetAddress());

        return companyToUpdate;
    }

    public Company findById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company with provided id: " + id + " doesn't exist"));
    }

    public void deleteById(Long id) {
        Company company = findById(id);
        List<Car> companyCars = carRepository.findAllByCompanyId(id);
        boolean areCarsRented = companyCars.stream()
                .anyMatch(c -> !c.isAvailable());
        if (areCarsRented) {
            throw new CarIsRentedException("Company cars are still rented!");
        }
        companyRepository.deleteById(id);
    }
}
