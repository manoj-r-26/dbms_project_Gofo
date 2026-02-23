package Dashboards;

import app.Main;
import managers.BookingManager;
import models.Booking;
import models.User;
import utils.UserRegistry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AdminDashboard extends JFrame {
    private DefaultListModel<String> userListModel;
    private DefaultListModel<String> groundListModel;
    private DefaultListModel<String> bookingListModel;
    private DefaultListModel<String> cancelListModel;

    public AdminDashboard() {
        setTitle("GoFo - Admin Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(new Color(25, 25, 25));

        JLabel title = new JLabel("👨‍💻 Admin Dashboard");
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        centerPanel.setBackground(new Color(25, 25, 25));

        userListModel = new DefaultListModel<>();
        JList<String> userList = new JList<>(userListModel);
        setupList(userList);
        JScrollPane userScroll = new JScrollPane(userList);
        centerPanel.add(createSection("👥 All Users", userScroll));

        groundListModel = new DefaultListModel<>();
        JList<String> groundList = new JList<>(groundListModel);
        setupList(groundList);
        JScrollPane groundScroll = new JScrollPane(groundList);
        centerPanel.add(createSection("🏟 All Playgrounds", groundScroll));

        bookingListModel = new DefaultListModel<>();
        JList<String> bookingList = new JList<>(bookingListModel);
        setupList(bookingList);
        JScrollPane bookingScroll = new JScrollPane(bookingList);
        centerPanel.add(createSection("📖 All Bookings", bookingScroll));

        cancelListModel = new DefaultListModel<>();
        JList<String> cancelList = new JList<>(cancelListModel);
        setupList(cancelList);
        JScrollPane cancelScroll = new JScrollPane(cancelList);

        JButton approveBtn = createButton("✅ Approve");
        JButton rejectBtn = createButton("❌ Reject");

        approveBtn.addActionListener((ActionEvent e) -> {
            int index = cancelList.getSelectedIndex();
            if (index != -1) {
                Booking booking = BookingManager.getInstance().getCancelRequests().get(index);
                double refund = booking.getHours() * 50;
                booking.getPlayer().setWallet(booking.getPlayer().getWallet() + refund);
                booking.getPlayground().getOwner().setWallet(booking.getPlayground().getOwner().getWallet() - refund);
                BookingManager.getInstance().cancelBooking(booking);
                updateLists();
                JOptionPane.showMessageDialog(null, "Booking cancelled and refund processed.");
            }
        });

        rejectBtn.addActionListener((ActionEvent e) -> {
            int index = cancelList.getSelectedIndex();
            if (index != -1) {
                Booking b = BookingManager.getInstance().getCancelRequests().get(index);
                BookingManager.getInstance().rejectCancelRequest(b);
                updateLists();
                JOptionPane.showMessageDialog(null, "Cancel request rejected.");
            }
        });

        JPanel cancelPanel = createSection("❗ Cancel Requests", cancelScroll);
        cancelPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cancelPanel.add(approveBtn);
        cancelPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cancelPanel.add(rejectBtn);

        centerPanel.add(cancelPanel);

        JButton logoutBtn = createButton("⬅ Logout");
        logoutBtn.setPreferredSize(new Dimension(200, 50));
        logoutBtn.addActionListener(e -> {
            Main.main(null);
            dispose();
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(25, 25, 25));
        bottomPanel.add(logoutBtn);

        outerPanel.add(title, BorderLayout.NORTH);
        outerPanel.add(centerPanel, BorderLayout.CENTER);
        outerPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(outerPanel);
        updateLists();
        setVisible(true);
    }

    private void updateLists() {
        userListModel.clear();
        for (User u : UserRegistry.getAll().values()) {
            userListModel.addElement(u.getRole() + ": " + u.getUsername());
        }

        groundListModel.clear();
        BookingManager.getInstance().getAllBookings().forEach(b -> {
            groundListModel.addElement(b.getPlayground().getName() + " (Owner: " + b.getPlayground().getOwner().getUsername() + ")");
        });

        bookingListModel.clear();
        for (Booking b : BookingManager.getInstance().getAllBookings()) {
            bookingListModel.addElement("Player: " + b.getPlayer().getUsername() + " - " + b.getPlayground().getName() + " - " + b.getHours() + " hr(s)");
        }

        cancelListModel.clear();
        for (Booking b : BookingManager.getInstance().getCancelRequests()) {
            cancelListModel.addElement("Player: " + b.getPlayer().getUsername() + " - " + b.getPlayground().getName() + " - " + b.getHours() + " hr(s)");
        }
    }

    private JPanel createSection(String title, JScrollPane scrollPane) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(25, 25, 25));

        JLabel label = new JLabel(title);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(scrollPane);
        return panel;
    }

    private void setupList(JList<String> list) {
        list.setFont(new Font("Arial", Font.PLAIN, 16));
        list.setForeground(Color.WHITE);
        list.setBackground(new Color(35, 35, 35));
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setBackground(new Color(60, 60, 60));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(90, 90, 90));
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(60, 60, 60));
                btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        return btn;
    }
}
