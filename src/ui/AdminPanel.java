package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DBConnection;
import model.Question;

public class AdminPanel extends JPanel {
    private MainFrame mainFrame;
    private JPanel contentArea;
    private CardLayout cardLayout;

    // Form Inputs
    private JTextField qText, optA, optB, optC, optD;
    private JComboBox<String> correctOpt, diffLevel;
    private JTable reportTable;

    public AdminPanel(MainFrame frame) {
        this.mainFrame = frame;
        setLayout(new BorderLayout());

        // 1. THE SIDEBAR (Dark & Modern)
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Theme.SECONDARY);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(new EmptyBorder(20, 0, 0, 0));

        // Sidebar Title
        JLabel brand = new JLabel("ADMIN PANEL");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 20));
        brand.setForeground(Color.WHITE);
        brand.setAlignmentX(Component.CENTER_ALIGNMENT);
        brand.setBorder(new EmptyBorder(0, 0, 30, 0));
        sidebar.add(brand);

        // Sidebar Buttons
        addSidebarButton("Manage Questions", "AddQuestion", sidebar);
        addSidebarButton("View Reports", "ViewReports", sidebar);

        sidebar.add(Box.createVerticalGlue()); // Push logout to bottom

        JButton logoutBtn = new JButton("Log Out");
        styleSidebarButton(logoutBtn);
        logoutBtn.setBackground(Theme.DANGER); // Red for logout
        logoutBtn.addActionListener(e -> frame.logout());
        sidebar.add(logoutBtn);

        add(sidebar, BorderLayout.WEST);

        // 2. THE CONTENT AREA (Light & Clean)
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(Theme.BACKGROUND);
        contentArea.setBorder(new EmptyBorder(20, 20, 20, 20)); // Padding around content

        contentArea.add(createAddQuestionPanel(), "AddQuestion");
        contentArea.add(createReportPanel(), "ViewReports");

        add(contentArea, BorderLayout.CENTER);
    }

    // --- HELPER METHODS FOR SIDEBAR ---
    private void addSidebarButton(String text, String cardName, JPanel sidebar) {
        JButton btn = new JButton(text);
        styleSidebarButton(btn);
        btn.addActionListener(e -> cardLayout.show(contentArea, cardName));
        sidebar.add(btn);
        sidebar.add(Box.createVerticalStrut(10)); // Gap
    }

    private void styleSidebarButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(Theme.SECONDARY);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setMaximumSize(new Dimension(200, 40));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false); // Transparent background look
        btn.setOpaque(true);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT); // Center buttons

        // Hover Effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!btn.getText().equals("Log Out"))
                    btn.setBackground(Theme.PRIMARY);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!btn.getText().equals("Log Out"))
                    btn.setBackground(Theme.SECONDARY);
            }
        });
    }

    // --- PANEL 1: ADD QUESTION (Card Style) ---
    private JPanel createAddQuestionPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);

        JLabel title = new JLabel("Add New Question");
        title.setFont(Theme.HEADER_FONT);
        title.setForeground(Theme.TEXT_DARK);
        title.setBorder(new EmptyBorder(0, 0, 20, 0));
        wrapper.add(title, BorderLayout.NORTH);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Form Fields
        addInput(card, gbc, 0, "Question Text:", qText = new JTextField());
        addInput(card, gbc, 1, "Option A:", optA = new JTextField());
        addInput(card, gbc, 2, "Option B:", optB = new JTextField());
        addInput(card, gbc, 3, "Option C:", optC = new JTextField());
        addInput(card, gbc, 4, "Option D:", optD = new JTextField());

        // Dropdowns
        JPanel row = new JPanel(new GridLayout(1, 2, 20, 0));
        row.setBackground(Color.WHITE);

        correctOpt = new JComboBox<>(new String[] { "A", "B", "C", "D" });
        row.add(createLabeledField("Correct Answer", correctOpt));

        diffLevel = new JComboBox<>(new String[] { "Beginner", "Intermediate", "Advanced" });
        row.add(createLabeledField("Difficulty", diffLevel));

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        card.add(row, gbc);

        // Save Button
        JButton saveBtn = new JButton("Save Question");
        Theme.styleButton(saveBtn, Theme.SUCCESS);
        saveBtn.addActionListener(e -> saveQuestion());

        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        card.add(saveBtn, gbc);

        wrapper.add(card, BorderLayout.CENTER);
        return wrapper;
    }

    // Helper for Inputs
    private void addInput(JPanel p, GridBagConstraints gbc, int y, String label, Component cmp) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        JLabel l = new JLabel(label);
        l.setFont(Theme.REGULAR_FONT);
        p.add(l, gbc);

        gbc.gridx = 1;
        if (cmp instanceof JTextField)
            Theme.styleTextField((JTextField) cmp);
        cmp.setPreferredSize(new Dimension(0, 35));
        p.add(cmp, gbc);
    }

    private JPanel createLabeledField(String label, JComponent cmp) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        JLabel l = new JLabel(label);
        l.setFont(Theme.REGULAR_FONT);
        l.setBorder(new EmptyBorder(0, 0, 5, 0));
        p.add(l, BorderLayout.NORTH);
        cmp.setPreferredSize(new Dimension(100, 35));
        cmp.setBackground(Color.WHITE);
        p.add(cmp, BorderLayout.CENTER);
        return p;
    }

    // --- PANEL 2: REPORTS (Custom Table) ---
    private JPanel createReportPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("High Score Reports");
        title.setFont(Theme.HEADER_FONT);
        title.setForeground(Theme.TEXT_DARK);

        JButton refreshBtn = new JButton("Refresh Data");
        Theme.styleButton(refreshBtn, Theme.PRIMARY);

        header.add(title, BorderLayout.WEST);
        header.add(refreshBtn, BorderLayout.EAST);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));
        wrapper.add(header, BorderLayout.NORTH);

        // Custom Table Styling
        String[] columns = { "Player Name", "Score", "Level", "Date Taken" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        reportTable = new JTable(model);
        reportTable.setRowHeight(30);
        reportTable.setFont(Theme.REGULAR_FONT);
        reportTable.setShowGrid(false);
        reportTable.setIntercellSpacing(new Dimension(0, 0));

        // Header Style
        JTableHeader th = reportTable.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 14));
        th.setBackground(Theme.SECONDARY);
        th.setForeground(Color.WHITE);
        th.setPreferredSize(new Dimension(0, 40));

        // Alternating Row Colors
        reportTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                }
                ((JLabel) c).setBorder(new EmptyBorder(0, 10, 0, 10)); // Cell padding
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(reportTable);
        scroll.setBorder(BorderFactory.createEmptyBorder()); // Remove ugly scroll border
        scroll.getViewport().setBackground(Color.WHITE);
        wrapper.add(scroll, BorderLayout.CENTER);

        refreshBtn.addActionListener(e -> loadReports(model));

        return wrapper;
    }

    // --- DATABASE LOGIC ---
    private void saveQuestion() {
        Question q = new Question(
                qText.getText(), optA.getText(), optB.getText(), optC.getText(), optD.getText(),
                (String) correctOpt.getSelectedItem(), (String) diffLevel.getSelectedItem());
        if (DBConnection.addQuestion(q)) {
            JOptionPane.showMessageDialog(this, "Success! Question added to database.");
            qText.setText("");
            optA.setText("");
            optB.setText("");
            optC.setText("");
            optD.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to save question.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadReports(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            ResultSet rs = DBConnection.getHighScores();
            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getString("username"), rs.getInt("score"), rs.getString("difficulty_level"),
                        rs.getString("date_taken")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}