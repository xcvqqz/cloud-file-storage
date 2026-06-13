package io.github.xcvqqz.cloud_file_storage.service;


import io.github.xcvqqz.cloud_file_storage.dto.UserRegistrationDTO;
import io.github.xcvqqz.cloud_file_storage.entity.User;
import io.github.xcvqqz.cloud_file_storage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public User save(UserRegistrationDTO userRegistrationDTO){
        if(userRegistrationDTO != null || userRegistrationDTO.)
       return userRepository.save(user);
    }


}
