package com.example.backend.services;

import com.example.backend.domain.entities.User;

import java.util.UUID;

public interface UserService {

    User getUserById(UUID id);
    User getUserByEmail(String email);

}
