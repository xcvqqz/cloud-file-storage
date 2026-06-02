package io.github.xcvqqz.cloud_file_storage.service;


import io.github.xcvqqz.cloud_file_storage.entity.User;
import io.github.xcvqqz.cloud_file_storage.repository.AbstractJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final AbstractJpaRepository abstractJpaRepository;

    @Autowired
    public UserService(AbstractJpaRepository abstractJpaRepository) {
        this.abstractJpaRepository = abstractJpaRepository;
    }

    public List<User> findAll(){
        return abstractJpaRepository.findAll();
    }

    public User save(User user){
       return abstractJpaRepository.save(user);
    }


}
