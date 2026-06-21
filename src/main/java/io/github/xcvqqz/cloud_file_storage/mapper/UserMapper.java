package io.github.xcvqqz.cloud_file_storage.mapper;


import io.github.xcvqqz.cloud_file_storage.dto.request.UserAuthenticationRequest;
import io.github.xcvqqz.cloud_file_storage.dto.request.UserRegistrationRequest;
import io.github.xcvqqz.cloud_file_storage.dto.response.UserAuthResponse;
import io.github.xcvqqz.cloud_file_storage.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User registrationToEntity(UserRegistrationRequest userRegistrationRequest);

    User authToEntity(UserAuthenticationRequest userAuthenticationRequest);

    UserAuthResponse entityToAuth(User user);

}
