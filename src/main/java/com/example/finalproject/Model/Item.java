package com.example.finalproject.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "Item")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Quantity")
    private Integer quantity;

    @Column(name = "Category")
    private String category;

    @Column(name = "Price")
    private float price;

    @Column(name = "Poster")
    private String poster;

    @ManyToOne
    @JoinColumn(name = "content_id")
    @JsonIgnore
    private Content content;
}
