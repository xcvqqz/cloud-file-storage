package io.github.xcvqqz.cloud_file_storage.mapper;


import io.github.xcvqqz.cloud_file_storage.dto.request.UserAuthenticationRequest;
import io.github.xcvqqz.cloud_file_storage.dto.response.UserAuthenticationResponse;
import io.github.xcvqqz.cloud_file_storage.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "authorities", target = "roles")
    UserAuthenticationResponse authenticationToResponse(Authentication authentication);

    @Mapping(source = "username", target = "name")
    @Mapping(source = "authorities", target = "roles")
    UserAuthenticationResponse userDetailsToResponse(UserDetails userDetails);

    UserAuthenticationRequest userToAuthenticationRequest(User user);

    default Set<String> map(Collection<? extends GrantedAuthority> authorities){
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

}