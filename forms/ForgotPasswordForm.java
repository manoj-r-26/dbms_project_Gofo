package forms;

import app.Main;
import dao.UserDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ForgotPasswordForm extends JFrame {
    public ForgotPasswordForm() {
        setTitle("GoFo - Reset Password");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.setBackground(new Color(25, 25, 25));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(25, 25, 25));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel title = new JLabel("🔒 Forgot Password");
        title.setFont(new Font("Segoe UI Black", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField usernameField = new JTextField();
        setupInput(usernameField, "Enter your username");

        JPasswordField newPasswordField = new JPasswordField();
        setupInput(newPasswordField, "Enter new password");

        JButton resetBtn = createButton("Reset Password");
        JButton backBtn = createButton("⬅ Back");

        resetBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String newPassword = new String(newPasswordField.getPassword()).trim();

            if (username.isEmpty() || username.equals("Enter your username") ||
                    newPassword.isEmpty() || newPassword.equals("Enter new password")) {
                JOptionPane.showMessageDialog(null, "Please fill all fields correctly.");
                return;
            }

            boolean success = UserDAO.getInstance().updatePassword(username, newPassword);

            if (success) {
                JOptionPane.showMessageDialog(null, "Password updated successfully!");
                Main.main(null);
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update password. Try again.");
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
        contentPanel.add(newPasswordField);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(resetBtn);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(backBtn);

        outerPanel.add(contentPanel);
        add(outerPanel);
        setVisible(true);
    }

    private void setupInput(JTextField field, String placeholder) {
        field.setMaximumSize(new Dimension(350, 40));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setBackground(new Color(40, 40, 40));
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70)));
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

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(new Color(60, 60, 60));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
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