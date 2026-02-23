package Dashboards;

import javax.swing.*;
import java.awt.*;

public class StaffDashboard extends JFrame {
    public StaffDashboard() {
        setTitle("🧑‍🔧 Staff Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.DARK_GRAY);

        JLabel welcome = new JLabel("Welcome, Staff! 👷");
        welcome.setForeground(Color.WHITE);
        add(welcome);
    }
}
