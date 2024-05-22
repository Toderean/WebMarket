package com.example.finalproject.Controller;

import com.example.finalproject.Model.*;
import com.example.finalproject.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private contentRepository contentRepo;

    @GetMapping("/{username}")
    public HashMap<Item, Integer> getItems(@PathVariable String username) {
        Optional<User> user = userRepository.findUserByUsername(username);
        HashMap<Item, Integer> temp = new HashMap<>();
        for (Item index : user.get().getCart().getItems()) {
            if (!temp.containsKey(index)) {
                Integer quantity = 0;
                for (Item aux : user.get().getCart().getItems()) {
                    if (index.equals(aux))
                        quantity++;
                }
                temp.put(index, quantity);
            }
        }
        return temp;
    }

    public List<Map<String, Object>> getItemsFrontEnd(@PathVariable String username) {
        Optional<User> user = userRepository.findUserByUsername(username);
        HashMap<Item, Integer> temp = new HashMap<>();
        for (Item index : user.get().getCart().getItems()) {
            if (!temp.containsKey(index)) {
                Integer quantity = 0;
                for (Item aux : user.get().getCart().getItems()) {
                    if (index.equals(aux))
                        quantity++;
                }
                temp.put(index, quantity);
            }
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<Item, Integer> entry : temp.entrySet()) {
            Map<String, Object> itemWithQuantity = new HashMap<>();
            itemWithQuantity.put("item", entry.getKey());
            itemWithQuantity.put("quantity", entry.getValue());
            result.add(itemWithQuantity);
        }
        return result;
    }


    @PostMapping("/add-to-cart/{itemID}/{quantity}/{username}")
    public ResponseEntity<String> addToCart(@PathVariable Integer itemID, @PathVariable Integer quantity, @PathVariable String username) {
        Optional<User> userOptional = userRepository.findUserByUsername(username);
        Optional<Item> itemOptional = itemRepository.findById(itemID);

        if (!userOptional.isPresent()) {
            System.out.println("User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        if (!itemOptional.isPresent()) {
            System.out.println("Item not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found");
        }

        User user = userOptional.get();
        Item item = itemOptional.get();

        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
        }

        try {
            for (int i = 0; i < quantity; i++) {
                cart.getItems().add(item);
            }
            cartRepository.save(cart);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Item added to cart successfully");
        } catch (Exception e) {
            System.out.println("Error adding item to cart: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding item to cart: " + e.getMessage());
        }
    }



    @DeleteMapping("/delete-from-cart/{itemId}/{username}")
    public ResponseEntity<String> deleteFromCart(@PathVariable Integer itemID, @PathVariable String username) {
        Optional<User> user = userRepository.findUserByUsername(username);
        Optional<Item> item = itemRepository.findById(itemID);
        if (user.isPresent() && item.isPresent()) {
            Cart cart = user.get().getCart();
            cart.getItems().remove(item.get());
            cartRepository.save(cart);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    public static void generateInvoice(Map<Item, Integer> items, String fileName) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = dateFormat.format(System.currentTimeMillis());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Invoice Date: " + dateTime + "\n\n");
            writer.write(String.format("%-20s%-10s%-10s\n", "Item", "Quantity", "Price"));
            writer.write("---------------------------------------------\n");
            double totalAmount = 0.0;
            for (Map.Entry<Item, Integer> entry : items.entrySet()) {
                Item item = entry.getKey();
                int quantity = entry.getValue();
                float price = item.getPrice();
                float itemTotal = price * quantity;
                writer.write(String.format("%-20s%-10d%-10.2f\n", item.getName(), quantity, itemTotal));
                totalAmount += itemTotal;
            }
            writer.write("---------------------------------------------\n");
            writer.write(String.format("%-30s%.2f\n", "Total Amount:", totalAmount));
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("razvantoderean@gmail.com");
        mailSender.send(message);
    }

    @PostMapping("/submit-command/{username}")
    public ResponseEntity<String> submit(@PathVariable String username) throws IOException {
        Optional<User> userOptional = userRepository.findUserByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            HashMap<Item, Integer> items = getItems(username);
            generateInvoice(items, user.getName());

            List<Item> orderedItems = new ArrayList<>();
            for (Map.Entry<Item, Integer> entry : items.entrySet()) {
                Item item = entry.getKey();
                Integer quantity = entry.getValue();
                item.setQuantity(item.getQuantity() - quantity);
                itemRepository.save(item);
                orderedItems.add(item);
            }

            Content content = new Content();
            content.setOrderItems(orderedItems);

            for (Item item : orderedItems) {
                item.setContent(content);
            }

            contentRepo.save(content);

            CommandsHistory commandsHistory = historyRepository.findByUserId(user.getId())
                    .orElseGet(() -> new CommandsHistory(user));

            content.setCommandsHistory(commandsHistory);
            commandsHistory.getItems().add(content);
            historyRepository.save(commandsHistory);

            user.getCart().getItems().clear();
            cartRepository.save(user.getCart());
            userRepository.save(user);
            sendEmail(user.getEmail(), "Invoice", "Your invoice has been created");
            return ResponseEntity.ok("Command submitted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/get-command-items/{id}")
    public ResponseEntity<List<Item>> getCommandItems(@PathVariable Integer id) {
        Optional<User> user = userRepository.findById(id);
        System.out.println("user founded : "  + user.get());
        if (user.isPresent()) {
            Optional<CommandsHistory> commandsHistory = historyRepository.findByUserId(user.get().getId());
            if (commandsHistory.isPresent()) {
                List<Content> contents = commandsHistory.get().getItems();
                List<Item> items = new ArrayList<>();
                for (Content content : contents) {
                    items.addAll(content.getOrderItems());
                }
                System.out.println("items founded : "  +items);
                return ResponseEntity.ok(items);
            }
            return ResponseEntity.ok(new ArrayList<>());
        }
        return ResponseEntity.notFound().build();
    }



}
