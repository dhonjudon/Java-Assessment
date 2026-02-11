package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

import db.DBConnection;
import model.Question;

public class PlayerPanel extends JPanel {
    private MainFrame mainFrame;
    private JPanel contentArea;
    private CardLayout cardLayout;
    
    // Quiz State
    private List<Question> currentQuestions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    
    // UI Components
    private JComboBox<String> levelBox;
    private JLabel qTextLabel, qCountLabel;
    private ButtonGroup optionsGroup;
    private JRadioButton optA, optB, optC, optD;

    public PlayerPanel(MainFrame frame) {
        this.mainFrame = frame;
        setLayout(new BorderLayout());

        // 1. THE SIDEBAR (Dark & Modern)
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Theme.SECONDARY);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(new EmptyBorder(20, 0, 0, 0));

        // Sidebar Profile Section
        JLabel profileIcon = new JLabel("PLAYER ZONE");
        profileIcon.setFont(new Font("Segoe UI", Font.BOLD, 18));
        profileIcon.setForeground(Theme.PRIMARY);
        profileIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel userLabel = new JLabel("Hi, " + frame.getCurrentUser().getUsername());
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(Color.LIGHT_GRAY);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userLabel.setBorder(new EmptyBorder(5, 0, 30, 0));

        sidebar.add(profileIcon);
        sidebar.add(userLabel);

        // Sidebar Navigation
        addSidebarButton("Dashboard", "Dashboard", sidebar);
        
        sidebar.add(Box.createVerticalGlue()); // Push logout to bottom
        
        JButton logoutBtn = new JButton("Log Out");
        styleSidebarButton(logoutBtn);
        logoutBtn.setBackground(Theme.DANGER); 
        logoutBtn.addActionListener(e -> frame.logout());
        sidebar.add(logoutBtn);

        add(sidebar, BorderLayout.WEST);

        // 2. THE CONTENT AREA
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(Theme.BACKGROUND);
        contentArea.setBorder(new EmptyBorder(30, 30, 30, 30));

        contentArea.add(createDashboard(), "Dashboard");
        contentArea.add(createQuizUI(), "Quiz");

        add(contentArea, BorderLayout.CENTER);
    }

    // --- PANEL 1: DASHBOARD ---
    private JPanel createDashboard() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.gridx = 0; gbc.gridy = 0;

        JLabel title = new JLabel("Start a New Quiz");
        title.setFont(Theme.HEADER_FONT);
        title.setForeground(Theme.TEXT_DARK);
        card.add(title, gbc);

        gbc.gridy = 1;
        JLabel sub = new JLabel("Select your difficulty level to begin.");
        sub.setFont(Theme.REGULAR_FONT);
        sub.setForeground(Color.GRAY);
        card.add(sub, gbc);

        gbc.gridy = 2;
        String[] levels = {"Beginner", "Intermediate", "Advanced"};
        levelBox = new JComboBox<>(levels);
        levelBox.setFont(Theme.REGULAR_FONT);
        levelBox.setBackground(Color.WHITE);
        levelBox.setPreferredSize(new Dimension(200, 35));
        card.add(levelBox, gbc);

        gbc.gridy = 3;
        JButton startBtn = new JButton("START QUIZ");
        Theme.styleButton(startBtn, Theme.SUCCESS);
        startBtn.setPreferredSize(new Dimension(200, 45));
        startBtn.addActionListener(e -> startQuiz());
        card.add(startBtn, gbc);

        wrapper.add(card);
        return wrapper;
    }

    // --- PANEL 2: QUIZ INTERFACE ---
    private JPanel createQuizUI() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);

        // Top Bar (Question Counter)
        qCountLabel = new JLabel("Question 1 of 5");
        qCountLabel.setFont(Theme.SUBHEADER_FONT);
        qCountLabel.setForeground(Theme.PRIMARY);
        qCountLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        wrapper.add(qCountLabel, BorderLayout.NORTH);

        // Question Card
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 10, 20);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;

        qTextLabel = new JLabel("Question text loads here...");
        qTextLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        qTextLabel.setForeground(Theme.TEXT_DARK);
        card.add(qTextLabel, gbc);

        // Options
        gbc.gridy = 1;
        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 10, 15));
        optionsPanel.setOpaque(false);
        optionsPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        optA = new JRadioButton(); optB = new JRadioButton(); 
        optC = new JRadioButton(); optD = new JRadioButton();
        styleRadio(optA); styleRadio(optB); styleRadio(optC); styleRadio(optD);

        optionsGroup = new ButtonGroup();
        optionsGroup.add(optA); optionsGroup.add(optB); 
        optionsGroup.add(optC); optionsGroup.add(optD);
        
        optionsPanel.add(optA); optionsPanel.add(optB);
        optionsPanel.add(optC); optionsPanel.add(optD);
        card.add(optionsPanel, gbc);

        // Next Button
        gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.EAST;
        JButton nextBtn = new JButton("Next Question >");
        Theme.styleButton(nextBtn, Theme.PRIMARY);
        nextBtn.addActionListener(e -> checkAnswerAndNext());
        card.add(nextBtn, gbc);

        wrapper.add(card, BorderLayout.CENTER);
        return wrapper;
    }

    // Helper Methods
    private void styleSidebarButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(Theme.SECONDARY);
        btn.setBorder(new EmptyBorder(12, 20, 12, 20));
        btn.setMaximumSize(new Dimension(220, 50));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if(!btn.getText().equals("Log Out")) btn.setBackground(Theme.PRIMARY);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if(!btn.getText().equals("Log Out")) btn.setBackground(Theme.SECONDARY);
            }
        });
    }

    private void styleRadio(JRadioButton rb) {
        rb.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        rb.setBackground(Color.WHITE);
        rb.setFocusPainted(false);
        rb.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void addSidebarButton(String text, String cardName, JPanel sidebar) {
        JButton btn = new JButton(text);
        styleSidebarButton(btn);
        btn.addActionListener(e -> cardLayout.show(contentArea, cardName));
        sidebar.add(btn);
        sidebar.add(Box.createVerticalStrut(10));
    }

    // --- LOGIC (Copied & pasted from original to ensure functionality) ---
    private void startQuiz() {
        String level = (String) levelBox.getSelectedItem();
        currentQuestions = DBConnection.getQuestionsByLevel(level);
        
        if (currentQuestions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No questions found for this level!");
            return;
        }
        
        currentQuestionIndex = 0;
        score = 0;
        showQuestion();
        cardLayout.show(contentArea, "Quiz");
    }

    private void showQuestion() {
        if (currentQuestionIndex < currentQuestions.size()) {
            Question q = currentQuestions.get(currentQuestionIndex);
            qCountLabel.setText("Question " + (currentQuestionIndex + 1) + " of " + currentQuestions.size());
            qTextLabel.setText(q.getQuestionText());
            
            optA.setText(q.getOptionA()); optA.setActionCommand("A");
            optB.setText(q.getOptionB()); optB.setActionCommand("B");
            optC.setText(q.getOptionC()); optC.setActionCommand("C");
            optD.setText(q.getOptionD()); optD.setActionCommand("D");
            
            optionsGroup.clearSelection();
        } else {
            finishQuiz();
        }
    }

    private void checkAnswerAndNext() {
        if (optionsGroup.getSelection() == null) {
            JOptionPane.showMessageDialog(this, "Please select an answer.");
            return;
        }
        Question q = currentQuestions.get(currentQuestionIndex);
        if (optionsGroup.getSelection().getActionCommand().equals(q.getCorrectOption())) {
            score++;
        }
        currentQuestionIndex++;
        showQuestion();
    }

    private void finishQuiz() {
        String level = (String) levelBox.getSelectedItem();
        DBConnection.saveScore(mainFrame.getCurrentUser().getUserId(), score, currentQuestions.size(), level);
        
        // Custom Styled Popup for Result
        String message = "<html><h2 style='color:#2ecc71'>Quiz Completed!</h2>" +
                         "<p>You scored <b>" + score + "</b> out of <b>" + currentQuestions.size() + "</b></p></html>";
        JOptionPane.showMessageDialog(this, message, "Result", JOptionPane.INFORMATION_MESSAGE);
        
        cardLayout.show(contentArea, "Dashboard");
    }
}