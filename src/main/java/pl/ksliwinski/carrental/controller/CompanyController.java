package pl.ksliwinski.carrental.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import pl.ksliwinski.carrental.model.Company;
import pl.ksliwinski.carrental.model.dto.CompanyDto;
import pl.ksliwinski.carrental.service.CompanyService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final ModelMapper modelMapper;

    @GetMapping()
    public List<CompanyDto> getAllCompanies(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "ASC") Sort.Direction direction, @RequestParam(defaultValue = "id") String by) {
        return companyService.getAll(page, size, direction, by).stream()
                .map(company -> modelMapper.map(company, CompanyDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{country}")
    public List<CompanyDto> getCompaniesByCountry(@PathVariable String country) {
        return companyService.getAllByCountry(country).stream()
                .map(company -> modelMapper.map(company, CompanyDto.class))
                .collect(Collectors.toList());
    }

    @PostMapping()
    public CompanyDto addCompany(@RequestBody CompanyDto companyDto) {
        return modelMapper.map(
                companyService.save(modelMapper.map(companyDto, Company.class)),
                CompanyDto.class);
    }

    @PutMapping("/{id}")
    public CompanyDto updateCompany(@PathVariable Long id, @RequestBody CompanyDto companyDto) {
        return modelMapper.map(
                companyService.update(id, modelMapper.map(companyDto, Company.class)),
                CompanyDto.class);
    }

    @DeleteMapping("/{id}")
    public void deleteCompany(@PathVariable Long id) {
        companyService.deleteById(id);
    }
}
