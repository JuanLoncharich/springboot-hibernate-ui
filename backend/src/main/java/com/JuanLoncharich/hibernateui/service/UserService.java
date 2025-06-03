package com.JuanLoncharich.hibernateui.service;

import com.JuanLoncharich.hibernateui.model.User;
import com.JuanLoncharich.hibernateui.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
