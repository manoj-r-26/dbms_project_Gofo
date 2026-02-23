package utils;

import models.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerRegistry {
    private static final Map<String, Player> players = new HashMap<>();

    public static void register(Player player) {
        players.put(player.getUsername(), player);
    }

    public static Player get(String username) {
        return players.get(username);
    }

    public static Map<String, Player> getAll() {
        return players;
    }
}