package Dashboards;

import app.Main;
import managers.BookingManager;
import managers.PlaygroundManager;
import models.Booking;
import models.Owner;
import models.Playground;
import utils.OwnerRegistry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class OwnerDashboard extends JFrame {
    private Owner owner;
    private DefaultListModel<String> ownedGroundsModel;
    private DefaultListModel<String> bookingModel;
    private JLabel walletLabel;

    public OwnerDashboard(Owner owner) {
        this.owner = OwnerRegistry.get(owner.getUsername());

        setTitle("GoFo - Owner Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(new Color(20, 20, 20));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(new Color(20, 20, 20));
        topPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));

        JLabel title = new JLabel("👨‍💼 Welcome, " + owner.getUsername());
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        walletLabel = new JLabel();
        walletLabel.setFont(new Font("Arial", Font.PLAIN, 22));
        walletLabel.setForeground(Color.LIGHT_GRAY);
        walletLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton refreshBtn = new JButton("🔄 Refresh Wallet");
        styleButton(refreshBtn);
        refreshBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        refreshBtn.setMaximumSize(new Dimension(200, 40));
        refreshBtn.addActionListener(e -> refreshWallet());

        topPanel.add(title);
        topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        topPanel.add(walletLabel);
        topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        topPanel.add(refreshBtn);
        topPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel addPanel = new JPanel();
        addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));
        addPanel.setBackground(new Color(30, 30, 30));
        addPanel.setMaximumSize(new Dimension(1000, 150));
        addPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        JTextField groundField = new JTextField();
        groundField.setBackground(new Color(30, 30, 30));
        groundField.setForeground(Color.WHITE);
        groundField.setCaretColor(Color.WHITE);
        groundField.setFont(new Font("Arial", Font.PLAIN, 20));
        groundField.setMaximumSize(new Dimension(900, 45));
        groundField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton addBtn = new JButton("➕ Add Playground");
        styleButton(addBtn);
        addBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        addBtn.addActionListener((ActionEvent e) -> {
            String name = groundField.getText().trim();
            if (!name.isEmpty()) {
                Playground pg = new Playground(name, OwnerRegistry.get(owner.getUsername()));
                PlaygroundManager.getInstance().addPlayground(pg);
                updateGroundList();
                updateBookings();
                groundField.setText("");
                JOptionPane.showMessageDialog(null, "Playground '" + name + "' added!");
            } else {
                JOptionPane.showMessageDialog(null, "Please enter a playground name.");
            }
        });

        addPanel.add(groundField);
        addPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        addPanel.add(addBtn);
        topPanel.add(addPanel);
        topPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 40, 0));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));
        centerPanel.setBackground(new Color(20, 20, 20));

        ownedGroundsModel = new DefaultListModel<>();
        JList<String> ownedList = new JList<>(ownedGroundsModel);
        ownedList.setFont(new Font("Arial", Font.PLAIN, 18));
        ownedList.setForeground(Color.WHITE);
        ownedList.setBackground(new Color(35, 35, 35));

        JLabel ownedLabel = new JLabel("🏟 Your Playgrounds");
        ownedLabel.setFont(new Font("Arial", Font.BOLD, 24));
        ownedLabel.setForeground(Color.WHITE);
        ownedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JScrollPane ownedScroll = new JScrollPane(ownedList);
        ownedScroll.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JPanel leftPanel = new JPanel(new BorderLayout(0, 10));
        leftPanel.setBackground(new Color(20, 20, 20));
        leftPanel.add(ownedLabel, BorderLayout.NORTH);
        leftPanel.add(ownedScroll, BorderLayout.CENTER);

        bookingModel = new DefaultListModel<>();
        JList<String> bookingList = new JList<>(bookingModel);
        bookingList.setFont(new Font("Arial", Font.PLAIN, 18));
        bookingList.setForeground(Color.WHITE);
        bookingList.setBackground(new Color(35, 35, 35));

        JLabel bookingsLabel = new JLabel("📖 Bookings on Your Grounds");
        bookingsLabel.setFont(new Font("Arial", Font.BOLD, 24));
        bookingsLabel.setForeground(Color.WHITE);
        bookingsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JScrollPane bookingScroll = new JScrollPane(bookingList);
        bookingScroll.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
        rightPanel.setBackground(new Color(20, 20, 20));
        rightPanel.add(bookingsLabel, BorderLayout.NORTH);
        rightPanel.add(bookingScroll, BorderLayout.CENTER);

        centerPanel.add(leftPanel);
        centerPanel.add(rightPanel);

        JButton logoutBtn = new JButton("⬅ Logout");
        styleButton(logoutBtn);
        logoutBtn.setPreferredSize(new Dimension(200, 50));
        logoutBtn.addActionListener(e -> {
            Main.main(null);
            dispose();
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(20, 20, 20));
        bottomPanel.add(logoutBtn);

        outerPanel.add(topPanel, BorderLayout.NORTH);
        outerPanel.add(centerPanel, BorderLayout.CENTER);
        outerPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(outerPanel);
        updateGroundList();
        updateBookings();
        refreshWallet();
        setVisible(true);
    }

    private void refreshWallet() {
        this.owner = OwnerRegistry.get(owner.getUsername()); // 💡 force update local reference
        walletLabel.setText("Wallet Balance: ₹" + owner.getWallet());
    }

    private void updateGroundList() {
        ownedGroundsModel.clear();
        for (Playground pg : PlaygroundManager.getInstance().getPlaygrounds()) {
            if (pg.getOwner() != null && pg.getOwner().getUsername().equals(owner.getUsername())) {
                ownedGroundsModel.addElement(pg.getName());
            }
        }
    }

    private void updateBookings() {
        bookingModel.clear();
        for (Booking b : BookingManager.getInstance().getAllBookings()) {
            if (b.getPlayground().getOwner() != null &&
                    b.getPlayground().getOwner().getUsername().equals(owner.getUsername())) {
                bookingModel.addElement("Player: " + b.getPlayer().getUsername()
                        + " - " + b.getPlayground().getName()
                        + " - " + b.getHours() + " hr(s)");
            }
        }
        refreshWallet();
    }

    private void styleButton(JButton btn) {
        Font font = new Font("Segoe UI", Font.PLAIN, 18);
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