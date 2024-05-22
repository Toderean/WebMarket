package com.example.finalproject.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@Table(name = "User")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "Email")
    private String email;

    @Column(name = "Name")
    private String name;

    @Column(name = "Surname")
    private String surname;

    @Column(name="Username")
    private String username;

    @Column(name = "Password")
    private String password;

    @Column(name = "UserType")
    private Integer userType;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    @JsonIgnore
    private Cart cart;

    public User(String name,String email, String username, String password, Integer userType) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", cart=" + (cart != null ? cart.getCartID() : null) +
                '}';
    }
}
