package com.example.CourseWork.services;

import com.example.CourseWork.models.Payment;
import com.example.CourseWork.models.User;
import com.example.CourseWork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void increaseMoney(Payment payment, Long userId) { // Увеличиваем кол-во денег пользователя
        User userEntity = findUserById(userId);
        userEntity.setMoney(userEntity.getMoney() + payment.getPrice());

        userRepository.save(userEntity);
    }

    public void save(User user) { // Сохраняем пользователя
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public User findUserById(Long id) {
        return userRepository.findOneById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }
}
