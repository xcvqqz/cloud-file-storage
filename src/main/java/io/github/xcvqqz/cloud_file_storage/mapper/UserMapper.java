package io.github.xcvqqz.cloud_file_storage.mapper;


import io.github.xcvqqz.cloud_file_storage.dto.UserRegistrationDTO;
import io.github.xcvqqz.cloud_file_storage.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User registrationToEntity(UserRegistrationDTO userRegistrationDTO);


//    User authToEntity(UserAuthDTO userAuthDTO);

}
