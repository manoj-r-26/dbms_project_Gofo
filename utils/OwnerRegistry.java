package utils;

import models.Owner;
import java.util.HashMap;
import java.util.Map;

public class OwnerRegistry {
    private static final Map<String, Owner> ownerMap = new HashMap<>();

    public static void register(Owner owner) {
        // Ensure we only store one instance per username
        if (!ownerMap.containsKey(owner.getUsername())) {
            ownerMap.put(owner.getUsername(), owner);
        }
    }

    public static Owner get(String username) {
        return ownerMap.get(username);
    }

    // Optional: method to update wallet directly (can be used from BookingManager)
    public static void addToWallet(String username, double amount) {
        Owner owner = ownerMap.get(username);
        if (owner != null) {
            owner.addToWallet(amount);
        }
    }
}