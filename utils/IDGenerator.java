package utils;

public class IDGenerator {
    private static int userId = 1000;

    public static int generateId() {
        return userId++;
    }
}