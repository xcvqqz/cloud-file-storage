package io.github.xcvqqz.cloud_file_storage.service;

import io.github.xcvqqz.cloud_file_storage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {       //является классом обёрткой над нашей сущность для аутентификации

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (UserDetails) userRepository.findByName(username).orElseThrow(() ->
                new UsernameNotFoundException(String.format("пользователь с именем %s не найден", username)));
    }
}
