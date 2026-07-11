package io.github.xcvqqz.cloud_file_storage.mapper;


import io.github.xcvqqz.cloud_file_storage.dto.response.UserAuthResponse;
import io.github.xcvqqz.cloud_file_storage.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.control.MappingControl;
import org.springframework.security.core.Authentication;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    UserAuthResponse entityToResponse(User user);

    UserAuthResponse authenticationToResponse(Authentication authentication);

    UserAuthResponse toResponse(String userName)

}
