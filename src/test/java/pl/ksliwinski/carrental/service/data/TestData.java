package pl.ksliwinski.carrental.service.data;

import pl.ksliwinski.carrental.model.Car;
import pl.ksliwinski.carrental.model.Company;
import pl.ksliwinski.carrental.model.Role;
import pl.ksliwinski.carrental.model.User;

import java.math.BigDecimal;

public class TestData {

    public User getUser(Long id) {
        User user = new User();
        user.setId(id);
        user.setFirstName("Karol");
        user.setLastName("Kowalski");
        user.setEmail("admin@gmail.com");
        user.setPassword("password");
        user.setRole(Role.ROLE_ADMIN);
        user.setEnabled(true);

        return user;
    }

    public Car getCar(Long id) {
        Car car = new Car();
        car.setId(id);
        car.setBrand("brand");
        car.setModel("model");
        car.setYear(2015);
        car.setColor("color");
        car.setDailyFee(BigDecimal.valueOf(150));
        car.setAvailable(true);
        car.setCompany(getCompany(1L));

        return car;
    }

    public Company getCompany(Long id) {
        Company company = new Company();
        company.setId(id);
        company.setName("company");
        company.setCountry("country");
        company.setCity("city");
        company.setStreetAddress("streetAddress");

        return company;
    }
}
