package ru.heroes.modeselector.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.heroes.modeselector.model.Game;
import ru.heroes.modeselector.model.Race;
import ru.heroes.modeselector.service.GamesService;

import java.util.HashMap;
import java.util.Map;

@Controller
public class GamesController {

    @Autowired
    private GamesService gamesService;

    @GetMapping(value = "/games")
    public String getGames(Model model) {
        model.addAttribute("activeGames", gamesService.getActiveGames());
        return "games";
    }

    @GetMapping(value = "/games/{id}")
    public String lobby(@PathVariable String id, Model model) {
        Game game = gamesService.getGame(id);
        model.addAttribute("game", game);
        model.addAttribute("races", gamesService.generate(game.getMode()));
        return "lobby";
    }

    @PostMapping(value = "/games/{id}")
    public String setResult(@PathVariable String id, @ModelAttribute("winner") String winner) {
        gamesService.getGame(id).setWinner(winner);
        return "redirect:/games";
    }

    @PostMapping(value = "/create-game")
    public String createGame(@ModelAttribute("newGame") Game game) {
        gamesService.addGame(game);
        return "redirect:/games";
    }

    @GetMapping(value = "games/{id}/select/{race}")
    public String select(@PathVariable String id, @PathVariable Race race, Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        gamesService.getGame(id).getPositions().put(name, race);
        return "redirect:/main/" + id;
    }

    @GetMapping(value = "/main/{id}")
    public String mainTable(@PathVariable String id, Model model) {
        Map<String, Race> positions = gamesService.getGame(id).getPositions();
        positions.forEach(model::addAttribute);
        return "main_table";
    }

    @GetMapping(value = "/reset")
    public String reset(){
        gamesService.getActiveGames().forEach(game -> game.setPositions(new HashMap<>()));
        return "redirect:/games";
    }
}
