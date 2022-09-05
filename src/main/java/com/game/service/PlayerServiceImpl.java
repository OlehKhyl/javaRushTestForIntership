package com.game.service;

import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl implements PlayerService{
    @Autowired
    PlayerRepository playerRepository;

    @Override
    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }
}
