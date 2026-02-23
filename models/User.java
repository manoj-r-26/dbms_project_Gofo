package models;

import models.Enums.Role;

public class User {
    private int id;
    private String username;
    private String password;
    private Role role;
    private double wallet;

    public User(int id, String username, String password, Role role, double wallet) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.wallet = wallet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {  // <-- THIS IS THE MISSING METHOD
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public double getWallet() {
        return wallet;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }

    public void addToWallet(double amount) {
        this.wallet += amount;
    }
}