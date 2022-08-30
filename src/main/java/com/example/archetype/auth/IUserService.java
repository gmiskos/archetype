package com.example.archetype.auth;

import com.example.archetype.controllers.users.UserDTO;
import com.example.archetype.entities.User;

public interface IUserService {
    ApplicationUser findByUsername(String username);
    void registerUser(UserDTO userDTO);
}
