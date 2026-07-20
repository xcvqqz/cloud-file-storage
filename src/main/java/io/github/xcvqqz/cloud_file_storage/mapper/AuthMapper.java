package io.github.xcvqqz.cloud_file_storage.mapper;


import io.github.xcvqqz.cloud_file_storage.dto.request.UserAuthenticationRequest;
import io.github.xcvqqz.cloud_file_storage.dto.response.UserAuthenticationResponse;
import io.github.xcvqqz.cloud_file_storage.entity.User;
import org.mapstruct.Mapper;
import org.springframework.security.core.Authentication;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    UserAuthenticationResponse entityToResponse(User user);

    UserAuthenticationResponse authenticationToResponse(Authentication authentication);

    UserAuthenticationResponse userNameToResponse(String userName);

    UserAuthenticationRequest userToAuthenticationRequest(User user);

}
