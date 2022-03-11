package pl.ksliwinski.carrental.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.ksliwinski.carrental.data.TestData;
import pl.ksliwinski.carrental.model.Car;
import pl.ksliwinski.carrental.model.Company;
import pl.ksliwinski.carrental.service.CarService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CarControllerTest {
    @MockBean
    private CarService carService;
    @Autowired
    private MockMvc mockMvc;
    private TestData testData = new TestData();
    private Car car1, car2;
    private Company company;

    @BeforeEach
    void setUp() {
        car1 = testData.getCar(1L);
        car2 = testData.getCar(2L);
        company = testData.getCompany(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnAllCars_whenGettingAllCars() throws Exception {
        when(carService.getAll(anyInt(), anyInt(), any(Sort.Direction.class), anyString()))
                .thenReturn(List.of(car1, car2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/cars")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(car1.getId().intValue())))
                .andExpect(jsonPath("$[1].id", is(car2.getId().intValue())));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnCar_whenGettingCarById() throws Exception {
        when(carService.findById(anyLong()))
                .thenReturn(car1);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/cars/{id}", car1.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id", is(car1.getId().intValue())))
                .andExpect(jsonPath("$.brand", is(car1.getBrand())))
                .andExpect(jsonPath("$.model", is(car1.getModel())))
                .andExpect(jsonPath("$.year", is(car1.getYear())))
                .andExpect(jsonPath("$.available", is(car1.isAvailable())));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldThrowEntityNotFound_whenGettingCarByNotExistingId() throws Exception {
        when(carService.findById(car1.getId()))
                .thenThrow(new EntityNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/cars/{id}", car1.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.timestamp", is(not(emptyString()))))
                .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(not(emptyString()))));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnAllCars_whenGettingCarsByCompany() throws Exception {
        when(carService.getAllByCompany(anyLong()))
                .thenReturn(List.of(car1, car2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/cars/company/{id}", company.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(car1.getId().intValue())))
                .andExpect(jsonPath("$[1].id", is(car2.getId().intValue())));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldThrowEntityNotFound_whenGettingCarsByNotExistingCompanyId() throws Exception {
        when(carService.getAllByCompany(anyLong()))
                .thenThrow(new EntityNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/cars/company/{id}", company.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.timestamp", is(not(emptyString()))))
                .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(not(emptyString()))));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnAllCars_whenGettingCarsByUserEmail() throws Exception {
        when(carService.getAllByUser(anyString()))
                .thenReturn(List.of(car1, car2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/cars/rented")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(car1.getId().intValue())))
                .andExpect(jsonPath("$[1].id", is(car2.getId().intValue())));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldThrowEntityNotFound_whenGettingCarsByNotExistingUserEmail() throws Exception {
        when(carService.getAllByUser(anyString()))
                .thenThrow(new EntityNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/cars/rented")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.timestamp", is(not(emptyString()))))
                .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(not(emptyString()))));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnForbidden_whenAddingCarByNonAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/cars/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(car1)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnForbidden_whenUpdatingCarByNonAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/cars/admin/{id}", car1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(car1)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnForbidden_whenDeletingCarByNonAdmins() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/cars/admin/{id}", car1.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnForbidden_whenDeletingCarByNonAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/cars/admin/{id}", car1.getId()))
                .andExpect(status().isForbidden());
    }
}