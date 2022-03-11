package pl.ksliwinski.carrental.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.ksliwinski.carrental.data.TestData;
import pl.ksliwinski.carrental.model.User;
import pl.ksliwinski.carrental.service.UserService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    private TestData testData = new TestData();
    private User user1, user2;

    @BeforeEach
    void setUp() {
        user1 = testData.getUser(1L);
        user2 = testData.getUser(2L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnAllUsers_whenGettingAllUsers() throws Exception {
        when(userService.getAll(anyInt(), anyInt(), any(Sort.Direction.class), anyString()))
                .thenReturn(List.of(user1, user2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(user1.getId().intValue())))
                .andExpect(jsonPath("$[1].id", is(user2.getId().intValue())));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnForbidden_whenGettingAllUsersByNonAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnNothing_whenDeletingUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/{id}", user1.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnForbidden_whenDeletingUserByNonAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/{id}", user1.getId()))
                .andExpect(status().isForbidden());
    }
}
