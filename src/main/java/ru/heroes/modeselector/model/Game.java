package ru.heroes.modeselector.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Game {
    private String id;
    private Status status = Status.ACTIVE;
    private Mode mode;
    private String winner;
    private Map<String, Race> positions = new HashMap<>();

    public Game(String id, Mode mode) {
        this.id = id;
        this.mode = mode;
    }
}
