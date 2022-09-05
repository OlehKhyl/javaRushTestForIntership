package com.game.controller;

import com.game.entity.Player;
import com.game.entity.PlayerRegister;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

@RestController
public class PlayerController {
    private final PlayerRepository repository;

    public PlayerController(PlayerRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/rest/players")
    List<Player> all(@RequestParam(required = false) String name,
                     @RequestParam(required = false) String title,
                     @RequestParam(required = false) Race race,
                     @RequestParam(required = false) Profession profession,
                     @RequestParam(required = false) Long after,
                     @RequestParam(required = false) Long before,
                     @RequestParam(required = false) Boolean banned,
                     @RequestParam(required = false) Integer minExperience,
                     @RequestParam(required = false) Integer maxExperience,
                     @RequestParam(required = false) Integer minLevel,
                     @RequestParam(required = false) Integer maxLevel,
                     @RequestParam(required = false) PlayerOrder order,
                     @RequestParam(required = false) Integer pageNumber,
                     @RequestParam(required = false) Integer pageSize) {

        List<Player> players = getQueue(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel);

        if (order != null) {
            if (order.equals(PlayerOrder.NAME)) {
                players.sort(Comparator.comparing(Player::getName));
            } else if (order.equals(PlayerOrder.EXPERIENCE)) {
                players.sort(Comparator.comparing(Player::getExperience));
            } else if (order.equals(PlayerOrder.BIRTHDAY)) {
                players.sort(Comparator.comparing(Player::getBirthday));
            } else if (order.equals(PlayerOrder.LEVEL)) {
                players.sort(Comparator.comparing(Player::getLevel));
            } else {
                players.sort(Comparator.comparing(Player::getId));
            }
        }

        pageSize = pageSize == null? 3 : pageSize;
        pageNumber = pageNumber == null? 0 : pageNumber;

        int begin = pageSize * pageNumber <= players.size() ? pageSize * pageNumber : players.size() - pageSize;
        int end = Math.min(pageSize * pageNumber + pageSize, players.size());
        players = players.subList(begin, end);

        return players;
    }


    @GetMapping("/rest/players/count")
    Integer count(@RequestParam(required = false) String name,
                  @RequestParam(required = false) String title,
                  @RequestParam(required = false) Race race,
                  @RequestParam(required = false) Profession profession,
                  @RequestParam(required = false) Long after,
                  @RequestParam(required = false) Long before,
                  @RequestParam(required = false) Boolean banned,
                  @RequestParam(required = false) Integer minExperience,
                  @RequestParam(required = false) Integer maxExperience,
                  @RequestParam(required = false) Integer minLevel,
                  @RequestParam(required = false) Integer maxLevel
    ) {
        List<Player> players = getQueue(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel);

        return players.size();
    }

    @GetMapping("/rest/players/{id}")
    ResponseEntity<Player> getById(@PathVariable("id") Long playerID) {
        if (playerID <= 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Player player = repository.findById(playerID).orElse(null);
        if (player != null) {
            return new ResponseEntity<>(player, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/rest/players")
    ResponseEntity<Player> create(@RequestBody PlayerRegister playerRegister) {
        Player player = playerRegister.createPlayer();
        if (player != null) {
            player = repository.save(player);
            return new ResponseEntity<>(player, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/rest/players/{id}")
    ResponseEntity<Player> updatePlayer(@PathVariable("id") Long playerId,
                                        @RequestBody PlayerRegister playerRegister) {
        if (playerId <= 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Player player = repository.findById(playerId).orElse(null);
        if (player != null) {
            try {
                player = playerRegister.updatePlayer(player);
                player = repository.save(player);
                return new ResponseEntity<>(player, HttpStatus.OK);
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/rest/players/{id}")
    ResponseEntity<String> delete(@PathVariable("id") Long playerID) {
        if (playerID <= 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Player player = repository.findById(playerID).orElse(null);
        if (player != null) {
            repository.delete(player);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    private List<Player> getQueue(@RequestParam(required = false) String name, @RequestParam(required = false) String title, @RequestParam(required = false) Race race, @RequestParam(required = false) Profession profession, @RequestParam(required = false) Long after, @RequestParam(required = false) Long before, @RequestParam(required = false) Boolean banned, @RequestParam(required = false) Integer minExperience, @RequestParam(required = false) Integer maxExperience, @RequestParam(required = false) Integer minLevel, @RequestParam(required = false) Integer maxLevel) {
        List<Player> players = repository.findAll();
        if (name != null) {
            players.retainAll(repository.findPlayerByNameContainingIgnoreCase(name));
        }
        if (title != null) {
            players.retainAll(repository.findPlayersByTitleContainsIgnoreCase(title));
        }
        if (race != null) {
            players.retainAll(repository.findPlayersByRace(race));
        }
        if (profession != null) {
            players.retainAll(repository.findPlayersByProfession(profession));
        }
        if (after != null) {
            players.retainAll(repository.findPlayersByBirthdayAfter(new Date(after)));
        }
        if (before != null) {
            players.retainAll(repository.findPlayersByBirthdayBefore(new Date(before)));
        }
        if (banned != null) {
            players.retainAll(repository.findPlayersByBanned(banned));
        }
        if (minExperience != null) {
            players.retainAll(repository.findPlayersByExperienceGreaterThan(minExperience));
        }
        if (maxExperience != null) {
            players.retainAll(repository.findPlayersByExperienceLessThan(maxExperience));
        }
        if (minLevel != null) {
            players.retainAll(repository.findPlayersByLevelGreaterThanEqual(minLevel));
        }
        if (maxLevel != null) {
            players.retainAll(repository.findPlayersByLevelLessThanEqual(maxLevel));
        }
        return players;
    }

}
