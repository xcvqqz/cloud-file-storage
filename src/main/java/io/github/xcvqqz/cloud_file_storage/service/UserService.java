package io.github.xcvqqz.cloud_file_storage.service;


import io.github.xcvqqz.cloud_file_storage.dto.UserAuthDTO;
import io.github.xcvqqz.cloud_file_storage.dto.UserRegistrationDTO;
import io.github.xcvqqz.cloud_file_storage.entity.User;
import io.github.xcvqqz.cloud_file_storage.mapper.UserMapper;
import io.github.xcvqqz.cloud_file_storage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    private final UserDetailsService userDetailsService;


    public List<User> findAll(){
        return userRepository.findAll();
    }


    @Transactional(readOnly = false)
    public User save(UserRegistrationDTO userRegistrationDTO){

        User newUser = userMapper.registrationToEntity(userRegistrationDTO);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        try{
          userRepository.save(newUser);
          UserDetailsImpl principal = UserDetailsImpl.build(user);
          userDetailsService.b
        } catch (DataIntegrityViolationException e){
            System.out.println("Такой пользователь уже создан");
        }
            return newUser;
    }

    @Transactional
    public User find(UserAuthDTO userAuthDTO){
        User user = userMapper.authToEntity(userAuthDTO);

    }


}
