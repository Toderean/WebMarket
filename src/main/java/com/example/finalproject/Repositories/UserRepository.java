package com.example.finalproject.Repositories;

import com.example.finalproject.Model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Integer> {
    Optional<User> findUserByUsername(String username);

    Optional<User> findUserById(Integer id);
}
