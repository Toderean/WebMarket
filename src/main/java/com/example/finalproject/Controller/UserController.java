package com.example.finalproject.Controller;

import com.example.finalproject.Model.Cart;
import com.example.finalproject.Model.User;
import com.example.finalproject.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@Service
public class UserController {
    @Autowired
    private UserRepository userRepository;


    private String encode(String password) {
        byte[] encodedHash = password.getBytes();
        StringBuilder encodedPassword = new StringBuilder();
        for (byte b : encodedHash) {
            b ^= 1;
            encodedPassword.append(b);
        }
        return encodedPassword.toString();
    }

    @PostMapping("/create")
    private ResponseEntity<User> createUser(@RequestBody User user) {
        User newUser = new User();
        newUser.setUserType(user.getUserType());
        newUser.setPassword(encode(user.getPassword()));
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setName(user.getName());
        userRepository.save(newUser);
        newUser.setCart(new Cart());
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    private ResponseEntity<User> logIn(@RequestParam String username,
                                       @RequestParam String password) {
        Optional<User> userOptional = userRepository.findUserByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println("Stored Encoded Password: " + user.getPassword());

            String encodedPassword = encode(password);
            System.out.println("Encoded Password from Input: " + encodedPassword);

            if (user.getPassword().equals(encodedPassword)) {
                System.out.println("Login Successful");
                return ResponseEntity.ok(user);
            } else {
                System.out.println("Incorrect Password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } else {
            System.out.println("User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @GetMapping("/user/{id}")
    private Optional<User> findUser(@PathVariable Integer id) {
        return userRepository.findUserById(id);
    }

    @PostMapping("/update/{id}")
    private ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User user) {
        Optional<User> userOptional = userRepository.findUserById(id);
        if (userOptional.isPresent()) {
            User newUser = userOptional.get();
            newUser.setName(user.getName());
            newUser.setEmail(user.getEmail());
            newUser.setUsername(user.getUsername());
            newUser.setPassword(encode(user.getPassword()));
            newUser.setUserType(user.getUserType());
            userRepository.save(newUser);
            return new ResponseEntity<>(newUser, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.CONFLICT);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
            return new ResponseEntity<>("User deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }
}
