package io.github.xcvqqz.cloud_file_storage.service;


import io.github.xcvqqz.cloud_file_storage.dto.request.UserAuthenticationRequest;
import io.github.xcvqqz.cloud_file_storage.dto.request.UserRegistrationRequest;
import io.github.xcvqqz.cloud_file_storage.dto.response.UserAuthResponse;
import io.github.xcvqqz.cloud_file_storage.entity.User;
import io.github.xcvqqz.cloud_file_storage.mapper.UserMapper;
import io.github.xcvqqz.cloud_file_storage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;


    public List<User> findAll(){
        return userRepository.findAll();
    }


    @Transactional(readOnly = false)
    public UserAuthResponse save(UserRegistrationRequest userRegistrationRequest){

        User newUser = userMapper.registrationToEntity(userRegistrationRequest);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        try{
          userRepository.save(newUser);
        } catch (DataIntegrityViolationException e){
            System.out.println("Такой пользователь уже создан");
        }
            return userMapper.entityToAuth(newUser);
    }


    public UserAuthResponse find(UserAuthenticationRequest userAuthenticationRequest){

      Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userAuthenticationRequest.name(),
                        userAuthenticationRequest.password()
                ));

        User authenticatedUser = (User) authentication.getPrincipal();

        return userMapper.entityToAuth(authenticatedUser);

    }


}
