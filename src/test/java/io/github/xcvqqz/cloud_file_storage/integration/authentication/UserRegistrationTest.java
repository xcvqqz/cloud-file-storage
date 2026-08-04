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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class UserRegistrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataFactory testDataFactory;

    @Autowired
    private ObjectMapper objectMapper;

//    @BeforeEach
//    void setUp(){
//        testDataFactory.clearAll();
//        testDataFactory.createUserWithRole();
//    }

    @Test
    @DisplayName("регистрация нового пользователя с валидными данными")
    public void shouldRegisterNewUser() throws Exception {
        Map<String, String> request =
                Map.of("name", "testname",
                        "password", "testpassword",
                        "confirmPassword", "testpassword");

        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("testname"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }


    @Test
    @DisplayName("ошибка несовпадающих паролей при регистрации")
    public void shouldThrowExceptionWhenPasswordAndConfirmPasswordDoNotMatch() throws Exception {
        Map<String, String> request =
                Map.of("name", "testname",
                        "password", "testpassword",
                        "confirmPassword", "wrongpassword");

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









}
