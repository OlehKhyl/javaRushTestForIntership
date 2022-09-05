package com.game.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class PlayerRegister {
    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    private String name;
    private String title;
    private Race race;
    private Profession profession;
    private Long birthday;
    private Boolean banned;
    private Integer experience;

    public PlayerRegister() {

    }

    public PlayerRegister(String name, String title, Race race, Profession profession, Long birthday, Boolean banned, Integer experience) {
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.birthday = birthday;
        this.banned = banned;
        this.experience = experience;
    }

    public Player createPlayer() {
        if (name != null && !name.isEmpty() && name.length() <= 12 && title != null && title.length() <= 30
                && race != null && profession != null && birthday != null && birthday >= 0 && experience != null
                && experience >= 0 && experience <= 10000000) {
            if (banned == null) banned = false;
            Player player = new Player(name, title, race, profession, new Date(birthday), banned, experience);
            return player;
        } return null;
    }

    public Player updatePlayer(Player player) {
        if (player != null) {
            if (name != null) {
                if (!name.isEmpty() && name.length() <= 12) {
                    player.setName(name);
                } else {
                    throw new IllegalArgumentException();
                }
            }
            if (title != null) {
                if (title.length() <= 30) {
                    player.setTitle(title);
                } else {
                    throw new IllegalArgumentException();
                }
            }
            if (race != null) {
                player.setRace(race);
            }
            if (profession != null) {
                player.setProfession(profession);
            }
            if (birthday != null) {
                if (birthday >= 0) {
                    player.setBirthday(new Date(birthday));
                } else {
                    throw new IllegalArgumentException();
                }
            }
            if (banned != null) {
                player.setBanned(banned);
            }
            if (experience != null) {
                if (experience >= 0 && experience <= 10000000) {
                    player.setExperience(experience);
                    player.setLevel(player.calculateCurrentLevel(experience));
                    player.setUntilNextLevel(player.calculateExperienceUntilNextLevel(player.getLevel(), experience));
                } else {
                    throw new IllegalArgumentException();
                }
            }
        }
        return player;
    }
}
