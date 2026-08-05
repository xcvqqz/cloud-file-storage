package io.github.xcvqqz.cloud_file_storage.integration.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.xcvqqz.cloud_file_storage.configuration.AbstractIntegrationTest;
import io.github.xcvqqz.cloud_file_storage.exception.PasswordMismatchException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import io.github.xcvqqz.cloud_file_storage.utils.TestDataFactory;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserRegistrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("успешная регистрация нового пользователя с валидными данными")
    public void shouldRegisterNewUser() throws Exception {
        Map<String, String> request =
                createRegistrationRequest("testname", "testpassword", "testpassword");

        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("testname"))
                .andExpect(jsonPath("$.roles[0]").value("USER"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }


    @Test
    @DisplayName("ошибка несовпадающих паролей при регистрации")
    public void shouldThrowExceptionWhenPasswordAndConfirmPasswordDoNotMatch() throws Exception {
        Map<String, String> request =
                createRegistrationRequest("testname", "testpassword", "wrongpassword");

        mockMvc.perform(post("/api/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Your password and confirm password do not match"))
                .andExpect(result ->
                        assertInstanceOf(PasswordMismatchException.class,
                                result.getResolvedException()));
    }

    @Test
    @DisplayName("возвращение статуса 400 при невалидном имени пользователя при регистрации")
    public void shouldFailWhenUsernameIsTooShort() throws Exception {
        Map<String, String> request
                = createRegistrationRequest("te", "testpassword", "testpassword");

        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("login should be min 3 and less 30 symbol"));
    }

    private Map<String, String> createRegistrationRequest(String name, String password, String confirmPassword){
        return Map.of("name", name,
                "password", password,
                "confirmPassword", confirmPassword);
    }









}
