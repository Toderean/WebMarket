package com.example.finalproject.Controller;

import com.example.finalproject.Model.Cart;
import com.example.finalproject.Model.Item;
import com.example.finalproject.Repositories.CartRepository;
import com.example.finalproject.Repositories.ItemRepository;
import com.example.finalproject.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @GetMapping("/all")
    public @ResponseBody Iterable<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @GetMapping("/all/filter=1")
    public @ResponseBody Iterable<Item> getAllItemsFilter() {
        ArrayList<Item> aux = new ArrayList<>((Collection) itemRepository.findAll());
        int n = aux.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (aux.get(j + 1).getPrice() < aux.get(j).getPrice()) {
                    Item temp = aux.get(j);
                    aux.set(j, aux.get(j + 1));
                    aux.set(j + 1, temp);
                }
            }
        }
        return aux;
    }

    @PostMapping(value = "/add-item")
    public ResponseEntity<Item> addNewItem(@RequestBody Item item) {
        Item n = new Item();
        n.setId((int) (itemRepository.count() + 1));
        n.setName(item.getName());
        n.setPrice(item.getPrice());
        n.setQuantity(item.getQuantity());
        n.setCategory(item.getCategory());
        n.setPoster(item.getPoster());
        itemRepository.save(n);
        return new ResponseEntity<>(n, HttpStatus.CREATED);
    }

    @GetMapping("/item/id={id}")
    public @ResponseBody Optional<Item> getItemById(@PathVariable Integer id) {
        return itemRepository.findById(id);
    }

    @GetMapping("/search={name}")
    public @ResponseBody Optional<Iterable<Item>> getItemByName(@PathVariable String name) {
        return itemRepository.findItemByName(name);
    }


    @PostMapping("/act/id={id}")
    public ResponseEntity<Item> updateItem(@PathVariable Integer id,
                                           @RequestBody Item item) {
        System.out.println(item.toString());
        try {
            Item updatedItem = itemRepository.findById(id).orElse(null);
            if (updatedItem != null) {
                updatedItem.setName(item.getName());
                updatedItem.setPoster(item.getPoster());
                updatedItem.setPrice(item.getPrice());
                updatedItem.setQuantity(item.getQuantity());
                updatedItem.setCategory(item.getCategory());
                itemRepository.save(updatedItem);
                return ResponseEntity.ok(updatedItem);
            } else {
                addNewItem(item);
                return ResponseEntity.ok(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/admin/deleteItems={id}")
    public ResponseEntity<String> deleteItem(@PathVariable Integer id){
        Iterable<Cart> carts = cartRepository.findAll();
        Item itemToDelete = itemRepository.findById(id).orElse(null);

        for (Cart cart: carts){
            cart.getItems().remove(itemToDelete);
            cartRepository.save(cart);
        }

        itemRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}