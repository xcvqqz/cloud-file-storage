package io.github.xcvqqz.cloud_file_storage.integration.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.xcvqqz.cloud_file_storage.configuration.AbstractIntegrationTest;
import io.github.xcvqqz.cloud_file_storage.exception.PasswordMismatchException;
import io.github.xcvqqz.cloud_file_storage.utils.TestDataFactory;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserAuthenticationTest extends AbstractIntegrationTest {

    private static final String INVALID_TEST_NAME = "InvalidTestName";
    private static final String INVALID_TEST_PASSWORD = "InvalidTestPassword";

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
                createAuthenticationRequest(testDataFactory.getTestUsername(), testDataFactory.getTestPassword());

        mockMvc.perform(post("/api/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(testDataFactory.getTestUsername()))
                .andExpect(jsonPath("$.roles[0]").value("USER"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @DisplayName("ошибка аутентификации пользователя с невалидными данными")
    public void shouldFailAuthenticationWithInvalidCredentials() throws Exception {
        Map<String, String> request =
                createAuthenticationRequest(INVALID_TEST_NAME, INVALID_TEST_PASSWORD);

        mockMvc.perform(post("/api/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @DisplayName("Успешное возвращение аутентифицированного пользователя")
    public void shouldReturnUserAuthenticationResponseWhenUserIsAuthenticated() throws Exception {
        Map<String, String> request =
                createAuthenticationRequest(testDataFactory.getTestUsername(), testDataFactory.getTestPassword());

        MvcResult loginResult = mockMvc.perform(
                        post("/api/auth/sign-in")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String sessionCookie = loginResult.getResponse().getCookie("SESSION").getValue();

        mockMvc.perform(get("/api/user/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("SESSION", sessionCookie)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(testDataFactory.getTestUsername()))
                .andExpect(jsonPath("$.roles[0]").value("USER"));
    }




    private Map<String, String> createAuthenticationRequest(String name, String password){
        return Map.of("name", name,
                "password", password);
    }




}
