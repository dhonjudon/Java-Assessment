package main;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;

import ui.MainFrame;
import ui.Theme;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // --- THE "GOATED" UI HACK ---
        // This styles every standard popup globally
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);
        UIManager.put("OptionPane.messageFont", Theme.REGULAR_FONT);
        UIManager.put("OptionPane.buttonFont", Theme.REGULAR_FONT);
        
        // Customizing the buttons inside popups
        UIManager.put("Button.background", Theme.PRIMARY);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0))); // Remove focus line
        
        // Make the top bar of popups match our theme
        JFrame.setDefaultLookAndFeelDecorated(true);
        // -----------------------------

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}