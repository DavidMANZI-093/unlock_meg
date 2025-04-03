package classes;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import interfaces.IGameView;

// Implementation of the game view using Swing
public class GameView implements IGameView {
    private JFrame frame;
    private JTextArea sceneTextArea;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel statusPanel;
    private JPanel actionButtonPanel;
    private JPanel rightContentPanel;
    private JScrollPane leftScrollPane;
    private JScrollPane rightScrollPane;
    private JLabel timeLabel;
    private JLabel inventoryLabel;
    private JProgressBar awarenessBar;
    // TODO: Add `private IGameController controller`;
    // This is a placeholder

    public GameView() {
        initializeUI();
    }

    private void initializeUI() {
        // TODO: Implement the UI
        // This is a placeholder

        // Colors
        Color lightBlack = new Color(8, 8, 8);
        Color darkGray = new Color(18, 18, 18);
        Color lightGreen = new Color(4, 194, 25);
        Color darkOrange = new Color(207, 116, 0);
        Color darkPurple = new Color(129, 98, 255);

        frame = new JFrame("Unlock - Mafia Escape");
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(1, 2, 0, 0));

        leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBackground(darkGray);

        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBackground(darkGray);
        
        rightContentPanel = new JPanel();
        rightContentPanel.setLayout(new BorderLayout());
        rightContentPanel.setBackground(darkGray);
        
        rightScrollPane = new JScrollPane();
        rightScrollPane.setPreferredSize(new Dimension(300, 300));

        leftPanel.setBackground(darkGray);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
        
        leftScrollPane = new JScrollPane();
        leftScrollPane.setPreferredSize(new Dimension(300, 200));
        leftScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Set up the scene description area
        sceneTextArea = new JTextArea();
        sceneTextArea.setEditable(false);
        sceneTextArea.setLineWrap(true);
        sceneTextArea.setWrapStyleWord(true);
        sceneTextArea.setBackground(lightBlack);
        sceneTextArea.setForeground(lightGreen);
        sceneTextArea.setFont(new Font("Lucida Console", Font.BOLD, 12));
        sceneTextArea.setMargin(new Insets(10, 10, 10, 10));

        leftScrollPane.setViewportView(sceneTextArea);
        leftPanel.add(leftScrollPane, BorderLayout.CENTER);        

        // Set up the action button panel
        actionButtonPanel = new JPanel();
        actionButtonPanel.setBackground(darkGray);
        actionButtonPanel.setLayout(new GridLayout(0, 1, 10, 10));
        actionButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        rightContentPanel.add(actionButtonPanel, BorderLayout.SOUTH);

        // Set up the status panel (inventory, time, awareness)
        statusPanel = new JPanel();
        statusPanel.setLayout(new GridLayout(3, 0, 0, 3));
        statusPanel.setBackground(darkGray);
        statusPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        // Inventory label
        inventoryLabel = new JLabel("Inventory: [Empty]");
        inventoryLabel.setForeground(darkPurple);
        inventoryLabel.setFont(new Font("Lucida Console", Font.BOLD, 12));
        statusPanel.add(inventoryLabel);

        // Time remaining label
        timeLabel = new JLabel("Time Remaining: 60 minutes");
        timeLabel.setForeground(darkOrange);
        timeLabel.setFont(new Font("Lucida Console", Font.BOLD, 12));
        statusPanel.add(timeLabel);

        // Guard awareness progress bar
        awarenessBar = new JProgressBar(0, 100);
        awarenessBar.setStringPainted(true);
        awarenessBar.setString("Guard Awareness: 0%");
        statusPanel.add(awarenessBar);
        
        rightContentPanel.add(statusPanel, BorderLayout.NORTH);

        rightScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rightScrollPane.setViewportView(rightContentPanel);

        rightPanel.add(rightScrollPane, BorderLayout.CENTER);

        frame.add(leftPanel);
        frame.add(rightPanel);
    }

    public void display() {
        frame.setVisible(true);
    }

    @Override
    public void setSceneText(String text) {
        sceneTextArea.setText(text);
        sceneTextArea.setCaretPosition(0); // Scroll to top
    }

    @Override
    public void setAvailableActions(List<String> actions) {
        // TODO: Clear existing buttons
        // This is a placeholder

        // Colors
        Color buttonBackground = new Color(19, 53, 112);
        Color buttonForeground = new Color(185, 185, 185);

        // Create new buttons for each action
        for (String action : actions) {
            JButton button = new JButton(action);
            button.setFocusPainted(false);
            button.setForeground(buttonForeground);
            button.setBackground(buttonBackground);
            button.setFont(new Font("Lucida Console", Font.BOLD, 12));
            // TODO: Add action listener to buttons
            // This is a placeholder

            actionButtonPanel.add(button);
        }

        // TODO: Dynamically refresh the panel
        // This is a placeholder
    }

    @Override
    public void setInventoryText(String text) {
        inventoryLabel.setText(text);
    }

    @Override
    public void setTimeAndAwareness(int timeRemaining, int guardAwareness) {
        // Colors
        Color awarenessBackground = new Color(38, 38, 38);
        Color awarenessGForeground = new Color(0, 150, 18);
        @SuppressWarnings("unused")
        Color awarenessRForeground = new Color(150, 0, 18);
        @SuppressWarnings("unused")
        Color awarenessYForeground = new Color(150, 150, 18);
        
        awarenessBar.setValue(guardAwareness);
        timeLabel.setText("ETA: " + timeRemaining + " min");
        awarenessBar.setBackground(awarenessBackground);
        awarenessBar.setForeground(awarenessGForeground);
        awarenessBar.setString("Guard Awareness: " + guardAwareness + "%");
        awarenessBar.setFont(new Font("Lucida Console", Font.CENTER_BASELINE, 12));
        awarenessBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        // TODO: Set the color of the progress bar based on the awareness level
        // This is a placeholder
    }

    @Override
    public void showGameOver(boolean isWon) {
        // TODO: Display a game over message
        // This is a placeholder
    }

    public static void main(String[] args) {
        GameView view = new GameView();
        
        view.setSceneText("You are in a dark room. There is a door to the north.");
        view.setInventoryText("Inventory: [ Key, Flashlight ]");
        view.setTimeAndAwareness(60, 30);
        view.setAvailableActions(List.of("Go North", "Use Key", "Use Flashlight"));

        view.display();
    }
}