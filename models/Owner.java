package models;

import utils.WalletStorage;

public class Owner extends User {

    public Owner(int id, String username, String password, double wallet) {
        super(id, username, password, Enums.Role.OWNER, wallet);
        WalletStorage.save(username, wallet); // Save initial value
    }

    public double getWallet() {
        return WalletStorage.load(getUsername());
    }

    public void setWallet(double amount) {
        WalletStorage.save(getUsername(), amount);
    }

    public void addToWallet(double amount) {
        double updated = getWallet() + amount;
        WalletStorage.save(getUsername(), updated);
    }

    public void reloadWallet() {
        setWallet(WalletStorage.load(getUsername()));
    }
}