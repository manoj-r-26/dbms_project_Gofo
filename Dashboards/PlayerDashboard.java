package Dashboards;

import app.Main;
import managers.BookingManager;
import managers.PlaygroundManager;
import models.Booking;
import models.Playground;
import models.Player;
import utils.RoundedPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class PlayerDashboard extends JFrame {
    private Player player;
    private DefaultListModel<String> availableModel;
    private DefaultListModel<String> bookedModel;
    private JLabel walletLabel;
    private javax.swing.Timer countdownTimer;
    private List<Playground> displayedPlaygrounds = new ArrayList<>();
    private JList<String> availableList;
    private JList<String> bookedList;

    public PlayerDashboard(Player player) {
        this.player = player;

        setTitle("GoFo - Player Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(new Color(20, 20, 20));

        JPanel topPanel = new RoundedPanel(25);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(new Color(20, 20, 20));
        topPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));

        JLabel title = new JLabel("\ud83c\udfc3 Welcome, " + player.getUsername());
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        walletLabel = new JLabel("Wallet: \u20b9" + player.getWallet());
        walletLabel.setFont(new Font("Arial", Font.BOLD, 22));
        walletLabel.setForeground(Color.LIGHT_GRAY);
        walletLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(title);
        topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        topPanel.add(walletLabel);
        topPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 40, 0));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));
        centerPanel.setBackground(new Color(20, 20, 20));

        availableModel = new DefaultListModel<>();
        availableList = new JList<>(availableModel);
        availableList.setFont(new Font("Arial", Font.BOLD, 18));
        availableList.setForeground(Color.WHITE);
        availableList.setBackground(new Color(35, 35, 35));

        JLabel availableLabel = new JLabel("\ud83d\udfe2 Available Playgrounds");
        availableLabel.setFont(new Font("Arial", Font.BOLD, 24));
        availableLabel.setForeground(Color.WHITE);

        UIManager.put("ComboBox.border", BorderFactory.createEmptyBorder());
        JComboBox<String> hourSelector = new JComboBox<>(new String[]{"1 Hour", "2 Hours", "3 Hours"});
        hourSelector.setFont(new Font("Arial", Font.BOLD, 18));
        hourSelector.setPreferredSize(new Dimension(150, 40));
        hourSelector.setBackground(new Color(30, 30, 30));
        hourSelector.setForeground(Color.WHITE);
        hourSelector.setUI(new javax.swing.plaf.basic.BasicComboBoxUI());
        hourSelector.setFocusable(false);

        JButton bookBtn = new JButton("Book");
        styleButton(bookBtn);
        bookBtn.setPreferredSize(new Dimension(120, 40));

        bookBtn.addActionListener((ActionEvent e) -> {
            int index = availableList.getSelectedIndex();
            if (index != -1 && index < displayedPlaygrounds.size()) {
                Playground pg = displayedPlaygrounds.get(index);
                int hours = hourSelector.getSelectedIndex() + 1;
                double cost = hours * 50;

                if (player.getWallet() >= cost) {
                    BookingManager.getInstance().book(player, pg, hours);
                    JOptionPane.showMessageDialog(null, "Booked for " + hours + " hour(s)!");
                    updateLists();
                } else {
                    JOptionPane.showMessageDialog(null, "Not enough balance.");
                }
            }
        });

        JPanel bookingControls = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bookingControls.setBackground(new Color(20, 20, 20));
        bookingControls.add(hourSelector);
        bookingControls.add(bookBtn);

        JPanel availablePanel = new RoundedPanel(25);
        availablePanel.setLayout(new BoxLayout(availablePanel, BoxLayout.Y_AXIS));
        availablePanel.setBackground(new Color(20, 20, 20));
        availablePanel.add(availableLabel);
        availablePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        availablePanel.add(new JScrollPane(availableList));
        availablePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        availablePanel.add(bookingControls);

        bookedModel = new DefaultListModel<>();
        bookedList = new JList<>(bookedModel);
        bookedList.setFont(new Font("Arial", Font.BOLD, 18));
        bookedList.setForeground(Color.WHITE);
        bookedList.setBackground(new Color(35, 35, 35));

        JLabel bookedLabel = new JLabel("\ud83d\udcd6 Your Bookings");
        bookedLabel.setFont(new Font("Arial", Font.BOLD, 24));
        bookedLabel.setForeground(Color.WHITE);

        JButton cancelBtn = new JButton("Request Cancel");
        styleButton(cancelBtn);
        cancelBtn.addActionListener((ActionEvent e) -> {
            int index = bookedList.getSelectedIndex();
            if (index != -1 && index < BookingManager.getInstance().getBookingsByPlayer(player).size()) {
                Booking b = BookingManager.getInstance().getBookingsByPlayer(player).get(index);
                BookingManager.getInstance().requestCancel(b);
                JOptionPane.showMessageDialog(null, "Cancel request sent to admin.");
                updateLists();
            }
        });

        JPanel bookedPanel = new RoundedPanel(25);
        bookedPanel.setLayout(new BoxLayout(bookedPanel, BoxLayout.Y_AXIS));
        bookedPanel.setBackground(new Color(20, 20, 20));
        bookedPanel.add(bookedLabel);
        bookedPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        bookedPanel.add(new JScrollPane(bookedList));
        bookedPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        bookedPanel.add(cancelBtn);

        centerPanel.add(availablePanel);
        centerPanel.add(bookedPanel);

        JButton logoutBtn = new JButton("\u2b05 Logout");
        styleButton(logoutBtn);
        logoutBtn.setPreferredSize(new Dimension(200, 50));
        logoutBtn.addActionListener(e -> {
            if (countdownTimer != null) countdownTimer.stop();
            Main.main(null);
            dispose();
        });

        JPanel bottomPanel = new RoundedPanel(25);
        bottomPanel.setBackground(new Color(20, 20, 20));
        bottomPanel.add(logoutBtn);

        outerPanel.add(topPanel, BorderLayout.NORTH);
        outerPanel.add(centerPanel, BorderLayout.CENTER);
        outerPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(outerPanel);
        updateLists();
        startCountdownTimer();
        setVisible(true);
    }

    private void updateLists() {
        int selectedIndex = availableList.getSelectedIndex();
        int bookedSelectedIndex = bookedList.getSelectedIndex();

        availableModel.clear();
        displayedPlaygrounds.clear();

        for (Playground pg : PlaygroundManager.getInstance().getPlaygrounds()) {
            if (pg.getOwner() != null && !pg.getOwner().getUsername().equals(player.getUsername())) {
                displayedPlaygrounds.add(pg);
                availableModel.addElement(pg.getName() + " (Owner: " + pg.getOwner().getUsername() + ")");
            }
        }

        if (selectedIndex >= 0 && selectedIndex < availableModel.size()) {
            availableList.setSelectedIndex(selectedIndex);
        }

        bookedModel.clear();
        for (Booking b : BookingManager.getInstance().getBookingsByPlayer(player)) {
            long elapsedMillis = Duration.between(b.getStartTime(), LocalDateTime.now()).toMillis();
            long totalMillis = b.getHours() * 3600000L;
            long remaining = totalMillis - elapsedMillis;
            String timer = remaining > 0 ? formatTime(remaining) : "Expired";
            bookedModel.addElement(b.getPlayground().getName() + " - " + timer);
        }

        if (bookedSelectedIndex >= 0 && bookedSelectedIndex < bookedModel.size()) {
            bookedList.setSelectedIndex(bookedSelectedIndex);
        }

        walletLabel.setText("Wallet: \u20b9" + player.getWallet());
    }

    private void startCountdownTimer() {
        countdownTimer = new javax.swing.Timer(1000, e -> updateLists());
        countdownTimer.start();
    }

    private String formatTime(long millis) {
        long seconds = millis / 1000;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long sec = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, sec);
    }

    private void styleButton(JButton btn) {
        Font font = new Font("Segoe UI", Font.BOLD, 18);
        Color base = new Color(60, 60, 60);
        Color hover = new Color(90, 90, 90);

        btn.setFont(font);
        btn.setBackground(base);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(hover);
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(base);
                btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }
}