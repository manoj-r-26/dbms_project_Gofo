package utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class WalletStorage {
    private static final String FILE = "wallets.txt";
    private static Map<String, Double> wallets = new HashMap<>();

    public static void save(String username, double wallet) {
        wallets.put(username, wallet);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE))) {
            for (Map.Entry<String, Double> entry : wallets.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double load(String username) {
        if (wallets.containsKey(username)) return wallets.get(username);
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)) {
                    double value = Double.parseDouble(parts[1]);
                    wallets.put(username, value);
                    return value;
                }
            }
        } catch (IOException ignored) {}
        return 0;
    }
}