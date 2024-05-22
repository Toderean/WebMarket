package com.example.finalproject.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Data
@Entity
@Table(name = "Cart")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CartID")
    private Long CartID;

    @OneToOne
    @JoinColumn(name = "UserId")
    @JsonBackReference
    private User user;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "cart_items",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    @JsonBackReference
    private List<Item> items = new ArrayList<>();

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + CartID +
                ", user=" + (user != null ? user.getUsername() : null) +
                ", items=" + items +
                '}';
    }
}