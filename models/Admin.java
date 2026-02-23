package models;

import models.Enums.Role;

public class Admin extends User {
    public Admin(int id, String username, String password) {
        super(id, username, password, Role.ADMIN, 0.0);
    }

    // Admin-specific tools can go here (e.g., delete users, view reports)
}