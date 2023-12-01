package Pruebas;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Blackbox Al Code Chat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel mainPanel = new JPanel(new BorderLayout());
        frame.add(mainPanel);

        JLabel titleLabel = new JLabel("Tendencias Hoy", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel moviePanel = new JPanel(new GridLayout(3, 2));
        mainPanel.add(moviePanel, BorderLayout.CENTER);

        JLabel familyRevoltedLabel = new JLabel("Familia Revuelta");
        familyRevoltedLabel.setFont(new Font("Arial", Font.BOLD, 16));
        moviePanel.add(familyRevoltedLabel);

        JLabel polvoFactsLabel = new JLabel("Hechos Polvo");
        polvoFactsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        moviePanel.add(polvoFactsLabel);

        JLabel openheimerLabel = new JLabel("Search Oppenheimer");
        openheimerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        moviePanel.add(openheimerLabel);

        JLabel christopherNolanLabel = new JLabel("Christopher Nolan Opensheim");
        christopherNolanLabel.setFont(new Font("Arial", Font.BOLD, 16));
        moviePanel.add(christopherNolanLabel);

        frame.setVisible(true);
    }
}