package com.example.finalproject.Repositories;

import com.example.finalproject.Model.Cart;
import com.example.finalproject.Model.User;
import org.springframework.data.repository.CrudRepository;

public interface CartRepository extends CrudRepository<Cart,Integer> {
}
