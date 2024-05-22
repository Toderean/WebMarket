package com.example.finalproject.Repositories;

import com.example.finalproject.Model.Item;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ItemRepository extends CrudRepository<Item,Integer> {
    Optional<Iterable<Item>> findItemByName(String name);
}
