package io.github.xcvqqz.cloud_file_storage.util;


import io.github.xcvqqz.cloud_file_storage.annotation.PasswordMatches;
import io.github.xcvqqz.cloud_file_storage.dto.request.UserRegistrationRequest;
import io.github.xcvqqz.cloud_file_storage.exception.PasswordMismatchException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatches, UserRegistrationRequest> {


    @Override
    public boolean isValid(UserRegistrationRequest value, ConstraintValidatorContext context) {

        context.disableDefaultConstraintViolation();

        if(value.password() == null || value.confirmPassword() == null){
            context.buildConstraintViolationWithTemplate
                    ("password and confirm password cant be null")
                    .addConstraintViolation();
                    return false;
        }

        if(!(value.password().equals(value.confirmPassword()))){
            context.buildConstraintViolationWithTemplate
                    ("password and confirm password dont match")
                    .addConstraintViolation();
                    return false;
        }

        return true;
    }
}
