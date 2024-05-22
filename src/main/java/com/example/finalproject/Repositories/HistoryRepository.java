package com.example.finalproject.Repositories;

import com.example.finalproject.Model.CommandsHistory;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface HistoryRepository extends CrudRepository<CommandsHistory,Integer> {
    Optional<CommandsHistory> findByUserId(Integer userID);
}
