package io.github.xcvqqz.cloud_file_storage.integration.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.xcvqqz.cloud_file_storage.configuration.AbstractIntegrationTest;
import io.github.xcvqqz.cloud_file_storage.exception.PasswordMismatchException;
import io.github.xcvqqz.cloud_file_storage.utils.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserAuthenticationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataFactory testDataFactory;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        testDataFactory.clearAll();
        testDataFactory.createUserWithRole();
    }

    @Test
    @DisplayName("успешная аутентификация пользователя с валидными данными")
    public void shouldAuthenticateSuccessfullyWithValidCredentials() throws Exception {
        Map<String, String> request =
                Map.of("name", "testname",
                        "password", "testpassword",
                        "confirmPassword", "testpassword");

        mockMvc.perform(post("/api/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(testDataFactory.getTestUsername()))
                .andExpect(jsonPath("$.password").doesNotExist());
    }











}
