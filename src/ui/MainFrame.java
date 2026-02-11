package ui;

import javax.swing.*;

import model.User;

import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private User currentUser;

    public MainFrame() {
        setTitle("Quiz Application & Score Manager");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add Screens
        mainPanel.add(new LoginPanel(this), "Login");
        
        add(mainPanel);
        showCard("Login");
    }

    public void showCard(String name) {
        cardLayout.show(mainPanel, name);
    }

    public void loginSuccess(User user) {
        this.currentUser = user;
        // Dynamically add the correct dashboard based on role
        if (user.isAdmin()) {
            mainPanel.add(new AdminPanel(this), "AdminDashboard");
            showCard("AdminDashboard");
        } else {
            mainPanel.add(new PlayerPanel(this), "PlayerDashboard");
            showCard("PlayerDashboard");
        }
    }
    
    public void logout() {
        this.currentUser = null;
        showCard("Login");
    }

    public User getCurrentUser() { return currentUser; }
}