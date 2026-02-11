package ui;

import javax.swing.*;
import java.awt.*;

import db.DBConnection;
import model.User;

public class LoginPanel extends JPanel {
    private JTextField userField;
    private JPasswordField passField;
    private MainFrame mainFrame;

    public LoginPanel(MainFrame frame) {
        this.mainFrame = frame;
        setLayout(new GridBagLayout()); // Centers everything
        setBackground(Theme.BACKGROUND); // Use our custom background color

        // --- The "Card" Container ---
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Theme.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1), // Subtle border
            BorderFactory.createEmptyBorder(40, 40, 40, 40) // Internal padding
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = new JLabel("Quiz App Login");
        title.setFont(Theme.HEADER_FONT);
        title.setForeground(Theme.PRIMARY);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        card.add(title, gbc);

        // Subtitle (Optional)
        JLabel subtitle = new JLabel("Welcome back! Please login.");
        subtitle.setFont(Theme.REGULAR_FONT);
        subtitle.setForeground(Color.GRAY);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        card.add(subtitle, gbc);

        // Username Label & Field
        gbc.gridy = 2; gbc.gridwidth = 1;
        JLabel uLabel = new JLabel("Username");
        uLabel.setFont(Theme.REGULAR_FONT);
        card.add(uLabel, gbc);

        userField = new JTextField(20);
        Theme.styleTextField(userField);
        gbc.gridy = 3; gbc.gridwidth = 2;
        card.add(userField, gbc);

        // Password Label & Field
        gbc.gridy = 4; gbc.gridwidth = 1;
        JLabel pLabel = new JLabel("Password");
        pLabel.setFont(Theme.REGULAR_FONT);
        card.add(pLabel, gbc);

        passField = new JPasswordField(20);
        Theme.styleTextField(passField);
        gbc.gridy = 5; gbc.gridwidth = 2;
        card.add(passField, gbc);

        // Buttons
        JButton btnLogin = new JButton("Login");
        Theme.styleButton(btnLogin, Theme.PRIMARY);

        JButton btnRegister = new JButton("Register");
        Theme.styleButton(btnRegister, Theme.SECONDARY);
        
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0)); // Split buttons evenly
        btnPanel.setBackground(Theme.CARD_BG);
        btnPanel.add(btnLogin);
        btnPanel.add(btnRegister);
        
        gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10); // More space above buttons
        card.add(btnPanel, gbc);

        // Actions
        btnLogin.addActionListener(e -> performLogin());
        btnRegister.addActionListener(e -> performRegister());

        add(card); // Add the card to the main panel
    }

    private void performLogin() {
        String u = userField.getText();
        String p = new String(passField.getPassword());
        User user = DBConnection.login(u, p);
        if (user != null) {
            mainFrame.loginSuccess(user);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performRegister() {
        String u = userField.getText();
        String p = new String(passField.getPassword());
        if(u.isEmpty() || p.isEmpty()) return;
        
        User newUser = new User(u, p, "player", "New Player");
        if(DBConnection.registerUser(newUser)) {
            JOptionPane.showMessageDialog(this, "Registered! Please Login.");
        } else {
            JOptionPane.showMessageDialog(this, "Error. Username may exist.");
        }
    }
}