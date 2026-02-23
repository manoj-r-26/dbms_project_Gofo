package forms;

import app.Main;
import dao.UserDAO;
import Dashboards.AdminDashboard;
import Dashboards.OwnerDashboard;
import Dashboards.PlayerDashboard;
import models.Enums.Role;
import models.Owner;
import models.Player;
import models.User;
import utils.OwnerRegistry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginForm extends JFrame {
    public LoginForm() {
        setTitle("GoFo - Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.setBackground(new Color(25, 25, 25));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(25, 25, 25));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel title = new JLabel("🔐 Login to GoFo");
        title.setFont(new Font("Segoe UI Black", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField usernameField = new JTextField();
        setupInput(usernameField, "Enter username");

        JPasswordField passwordField = new JPasswordField();
        setupPasswordField(passwordField, "Enter password");

        JComboBox<String> roleBox = new JComboBox<>(new String[]{"Select role", "PLAYER", "OWNER", "ADMIN"});
        styleComboBox(roleBox);

        JButton loginBtn = createButton("Login");
        JButton backBtn = createButton("⬅ Back");

        loginBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String selectedRole = (String) roleBox.getSelectedItem();

            if (username.isEmpty() || username.equals("Enter username") ||
                    password.isEmpty() || password.equals("Enter password") ||
                    selectedRole.equals("Select role")) {
                JOptionPane.showMessageDialog(null, "Please fill all fields correctly.");
                return;
            }

            Role role = Role.valueOf(selectedRole);
            User user = UserDAO.getInstance().login(username, password, role);

            if (user != null) {
                switch (role) {
                    case PLAYER -> new PlayerDashboard((Player) user).setVisible(true);
                    case OWNER -> {
                        Owner registeredOwner = OwnerRegistry.get(user.getUsername());
                        new OwnerDashboard(registeredOwner).setVisible(true);
                    }
                    case ADMIN -> new AdminDashboard().setVisible(true);
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid credentials or role.");
            }
        });

        backBtn.addActionListener(e -> {
            Main.main(null);
            dispose();
        });

        contentPanel.add(title);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(usernameField);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(passwordField);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(roleBox);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(loginBtn);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(backBtn);

        outerPanel.add(contentPanel);
        add(outerPanel);
        setVisible(true);
    }

    private void setupInput(JTextField field, String placeholder) {
        field.setMaximumSize(new Dimension(350, 40));
        field.setFont(new Font("Segoe UI", Font.BOLD, 16));
        field.setBackground(new Color(40, 40, 40));
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 1));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        field.setForeground(Color.GRAY);
        field.setText(placeholder);

        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.WHITE);
                }
            }

            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    private void setupPasswordField(JPasswordField field, String placeholder) {
        field.setMaximumSize(new Dimension(350, 40));
        field.setFont(new Font("Segoe UI", Font.BOLD, 16));
        field.setBackground(new Color(40, 40, 40));
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 1));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        field.setForeground(Color.GRAY);
        field.setEchoChar((char) 0);
        field.setText(placeholder);

        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (new String(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.WHITE);
                    field.setEchoChar('•');
                }
            }

            public void focusLost(FocusEvent e) {
                if (new String(field.getPassword()).isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                    field.setEchoChar((char) 0);
                }
            }
        });
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setMaximumSize(new Dimension(350, 40));
        comboBox.setFont(new Font("Segoe UI", Font.BOLD, 16));
        comboBox.setBackground(new Color(40, 40, 40));
        comboBox.setForeground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 1));
        comboBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setFont(new Font("Segoe UI", Font.BOLD, 16));
                label.setBackground(isSelected ? new Color(60, 60, 60) : new Color(40, 40, 40));
                label.setForeground(Color.WHITE);
                return label;
            }
        });
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(new Color(60, 60, 60));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(90, 90, 90), 1));
        btn.setMaximumSize(new Dimension(350, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(90, 90, 90));
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(60, 60, 60));
                btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        return btn;
    }
}