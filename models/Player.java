package models;

public class Player extends User {
    public Player(int id, String username, String password, double wallet) {
        super(id, username, password, Enums.Role.PLAYER, wallet);
    }

    // ✅ Add money to player's wallet (used for refund)
    public void addToWallet(double amount) {
        setWallet(getWallet() + amount);
    }

    // ✅ Deduct money from wallet (used when booking)
    public void deductFromWallet(double amount) {
        setWallet(getWallet() - amount);
    }
}