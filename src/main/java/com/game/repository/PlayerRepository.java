package com.game.repository;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findPlayerByNameContainingIgnoreCase(String name);
    List<Player> findPlayersByTitleContainsIgnoreCase(String title);
    List<Player> findPlayersByRace(Race race);
    List<Player> findPlayersByProfession(Profession profession);
    List<Player> findPlayersByBirthdayAfter(Date after);
    List<Player> findPlayersByBirthdayBefore(Date before);
    List<Player> findPlayersByBanned(Boolean banned);
    List<Player> findPlayersByExperienceGreaterThan(Integer minExperience);
    List<Player> findPlayersByExperienceLessThan(Integer maxExperience);
    List<Player> findPlayersByLevelGreaterThanEqual(Integer minLevel);
    List<Player> findPlayersByLevelLessThanEqual(Integer maxLevel);
}
