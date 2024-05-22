package com.example.finalproject.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "History")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CommandsHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "commandsHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Content> items = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "UserID")
    @JsonBackReference
    private User user;

    public CommandsHistory(User user) {
        this.user = user;
    }
}
