package com.example.backend.services;

import com.example.backend.domain.entities.User;

public interface UserService {

    User getUserByEmail(String email);

}
