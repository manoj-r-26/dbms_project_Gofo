package app;
import forms.LoginForm;
import forms.SignupForm;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import forms.ForgotPasswordForm;

public class Main {
    public static void main(String[] args) {
        try {
            // Use system L&F for best Mac font rendering
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("GoFo - Play Area Management System");
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setBackground(Color.BLACK);

            // Outer Panel to center contents
            JPanel outerPanel = new JPanel(new GridBagLayout());
            outerPanel.setBackground(new Color(20, 20, 20));

            // Inner content panel
            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBackground(new Color(20, 20, 20));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

            // ✅ LARGE BRIGHT TEXT
            JLabel title = new JLabel("🏟 GoFo - Play Area Management System");
            title.setFont(new Font("Arial", Font.BOLD, 36)); // fallback font for visibility
            title.setForeground(Color.WHITE);
            title.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Buttons
            JButton loginBtn = new JButton("🔐 Login");
            JButton signupBtn = new JButton("📝 Register");
            JButton forgotBtn = new JButton("Forgot Password?");

            Font btnFont = new Font("Segoe UI", Font.PLAIN, 18);
            Color btnColor = new Color(60, 60, 60);
            Color hoverColor = new Color(90, 90, 90);

            JButton[] buttons = {loginBtn, signupBtn, forgotBtn};
            for (JButton btn : buttons) {
                btn.setFont(btnFont);
                btn.setBackground(btnColor);
                btn.setForeground(Color.WHITE);
                btn.setFocusPainted(false);
                btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
                btn.setMaximumSize(new Dimension(350, 50));
                btn.setAlignmentX(Component.CENTER_ALIGNMENT);

                btn.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        btn.setBackground(hoverColor);
                        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    }

                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        btn.setBackground(btnColor);
                        btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                });
            }

            // Button Actions
            loginBtn.addActionListener((ActionEvent e) -> {
                new LoginForm().setVisible(true);
                frame.dispose();
            });

            signupBtn.addActionListener((ActionEvent e) -> {
                new SignupForm().setVisible(true);
                frame.dispose();
            });

            forgotBtn.addActionListener((ActionEvent e) -> {
                new ForgotPasswordForm().setVisible(true);
            });

            // Layout
            contentPanel.add(title);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 60)));
            contentPanel.add(loginBtn);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
            contentPanel.add(signupBtn);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
            contentPanel.add(forgotBtn);

            outerPanel.add(contentPanel);

            frame.add(outerPanel);
            frame.setVisible(true);
        });
    }
}