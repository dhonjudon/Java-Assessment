package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Theme {
    // 1. Color Palette (Modern "Flat" Look)
    public static final Color PRIMARY = new Color(52, 152, 219);    // Modern Blue
    public static final Color SECONDARY = new Color(44, 62, 80);    // Dark Blue/Grey
    public static final Color BACKGROUND = new Color(236, 240, 241); // Soft Grey (Not white)
    public static final Color CARD_BG = Color.WHITE;
    public static final Color TEXT_DARK = new Color(44, 62, 80);
    public static final Color SUCCESS = new Color(46, 204, 113);    // Green
    public static final Color DANGER = new Color(231, 76, 60);      // Red

    // 2. Standard Fonts
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font SUBHEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    // 3. Helper to Style Buttons
    public static void styleButton(JButton btn, Color bg) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false); // Removes the ugly focus line
        btn.setBorder(new EmptyBorder(10, 20, 10, 20)); // Padding inside button
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Simple Hover Effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg);
            }
        });
    }

    // 4. Helper to Style TextFields
    public static void styleTextField(JTextField txt) {
        txt.setFont(REGULAR_FONT);
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            new EmptyBorder(5, 10, 5, 10) // Padding text inside box
        ));
    }
}