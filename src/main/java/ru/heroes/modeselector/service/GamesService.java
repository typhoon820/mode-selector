package ru.heroes.modeselector.service;

import org.springframework.stereotype.Service;
import ru.heroes.modeselector.model.Game;
import ru.heroes.modeselector.model.Mode;
import ru.heroes.modeselector.model.Race;
import ru.heroes.modeselector.model.Status;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GamesService {

    private static final Random RNG = new Random(System.currentTimeMillis());
    private List<Game> games = Collections.singletonList(new Game("1", Mode.SINGLE_DRAFT));

    public List<Game> getActiveGames() {
        return games.stream()
                .filter(game -> game.getStatus() == Status.ACTIVE)
                .collect(Collectors.toList());
    }

    public void addGame(Game game) {
        games.add(game);
    }

    public Game getGame(String id) {
        return games.stream()
                .filter(game -> game.getId().equals(id))
                .findFirst().orElse(null);
    }

    public List<Race> generate(Mode mode) {
        List<Race> races = new ArrayList<>();
        LinkedList<Race> allRaces = new LinkedList<>(Arrays.asList(Race.values()));
        if (mode == Mode.SINGLE_DRAFT) {
            races.add(allRaces.remove(RNG.nextInt(allRaces.size())));
            races.add(allRaces.remove(RNG.nextInt(allRaces.size())));
            races.add(allRaces.remove(RNG.nextInt(allRaces.size())));
        }
        return races;
    }

}
