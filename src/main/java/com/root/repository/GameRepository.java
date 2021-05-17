package com.root.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.root.entity.Game;

public interface GameRepository extends JpaRepository<Game, Integer> {
    Game findById(int id);

}