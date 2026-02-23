package utils;

import models.User;

import java.util.HashMap;
import java.util.Map;

public class UserRegistry {
    private static final Map<String, User> userMap = new HashMap<>();

    public static void register(User user) {
        userMap.put(user.getUsername(), user);
    }

    public static User get(String username) {
        return userMap.get(username);
    }

    public static Map<String, User> getAll() {
        return userMap;
    }
}