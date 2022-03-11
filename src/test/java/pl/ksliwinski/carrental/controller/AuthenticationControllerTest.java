package pl.ksliwinski.carrental.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.ksliwinski.carrental.data.TestData;
import pl.ksliwinski.carrental.exception.EmailAlreadyTakenException;
import pl.ksliwinski.carrental.model.User;
import pl.ksliwinski.carrental.service.AuthenticationService;

import javax.persistence.EntityNotFoundException;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {
    @MockBean
    private AuthenticationService authenticationService;
    @Autowired
    private MockMvc mockMvc;
    private TestData testData = new TestData();
    private User user;

    @BeforeEach
    void setUp() {
        user = testData.getUser(1L);
    }

    @Test
    void shouldReturnUser_whenRegistering() throws Exception {
        when(authenticationService.registerUser(any(User.class)))
                .thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    void shouldThrowEmailAlreadyTaken_whenRegisteringWithExistingEmail() throws Exception {
        when(authenticationService.registerUser(any(User.class)))
                .thenThrow(EmailAlreadyTakenException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.timestamp", is(not(emptyString()))))
                .andExpect(jsonPath("$.status", is(HttpStatus.CONFLICT.value())))
                .andExpect(jsonPath("$.message", is(not(emptyString()))));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnString_whenVerifyingToken() throws Exception {
        when(authenticationService.verifyAccount(anyString()))
                .thenReturn("Account successfully activated!");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/verifyAccount/{token}", "token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldEntityNotFound_whenVerifyingWithInvalidToken() throws Exception {
        when(authenticationService.verifyAccount(anyString()))
                .thenThrow(EntityNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/verifyAccount/{token}", "token"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.timestamp", is(not(emptyString()))))
                .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(not(emptyString()))));
    }
}
