package dao;

import models.Enums.Role;
import models.Owner;
import models.Player;
import models.User;
import utils.DB;
import utils.OwnerRegistry;
import utils.PlayerRegistry;
import utils.UserRegistry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
    private static UserDAO instance;

    private UserDAO() {}

    public static UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }

    public boolean exists(String username) {
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE username = ?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void register(User user) {
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO users (username, password, role, wallet) VALUES (?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole().toString());
            stmt.setDouble(4, user.getWallet());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                int id = keys.getInt(1);
                user.setId(id);

                switch (user.getRole()) {
                    case PLAYER -> PlayerRegistry.register((Player) user);
                    case OWNER -> OwnerRegistry.register((Owner) user);
                    case ADMIN -> UserRegistry.register(user);
                    default -> throw new IllegalArgumentException("Unexpected role: " + user.getRole());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User login(String username, String password, Role role) {
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM users WHERE username = ? AND password = ? AND role = ?")) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role.toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                double wallet = rs.getDouble("wallet");
                User user;

                switch (role) {
                    case PLAYER -> {
                        user = new Player(id, username, password, wallet);
                        PlayerRegistry.register((Player) user);
                    }
                    case OWNER -> {
                        user = new Owner(id, username, password, wallet);
                        OwnerRegistry.register((Owner) user);
                    }
                    case ADMIN -> {
                        user = new User(id, username, password, Role.ADMIN, wallet);
                        UserRegistry.register(user);
                    }
                    default -> {
                        return null;
                    }
                }

                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getByUsername(String username, Role role) {
        return switch (role) {
            case PLAYER -> PlayerRegistry.get(username);
            case OWNER -> OwnerRegistry.get(username);
            case ADMIN -> UserRegistry.get(username);
            default -> null;
        };
    }

    // ✅ Added method
    public boolean updatePassword(String username, String newPassword) {
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE users SET password = ? WHERE username = ?")) {
            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}