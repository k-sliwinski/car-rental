package pl.ksliwinski.carrental.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ksliwinski.carrental.model.Company;
import pl.ksliwinski.carrental.repository.CompanyRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

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
        //TODO check if any cars of this company is rented
        companyRepository.deleteById(id);
    }
}
