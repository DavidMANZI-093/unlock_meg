package classes;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.application.Platform;
import java.util.List;

import interfaces.IGameView;

// Implementation of the game view using JavaFX
public class JFXGameView extends Application implements IGameView {
    private Stage stage;
    private TextArea sceneTextArea;
    private BorderPane leftPanel;
    private BorderPane rightPanel;
    private VBox statusPanel;
    private VBox actionButtonPanel;
    private BorderPane rightContentPanel;
    private ScrollPane leftScrollPane;
    private ScrollPane rightScrollPane;
    private Label timeLabel;
    private Label inventoryLabel;
    private ProgressBar awarenessBar;
    private Label awarenessLabel;
    // TODO: Add `private IGAMEController controller;`
    // This is a placeholder

    public JFXGameView() {
        // TODO: Contructor for non-Javafx configurations
        // This is a placeholder -(The rest are done in the `start() Override...`)
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        initializeUI();
        display();

        // Ensure UI updates happen after JavaFX initializes
        Platform.runLater(() -> {
            setSceneText("You are in a dark room. There is a door to the north.");
            setInventoryText("Inventory: [ Key, Flashlight ]");
            setTimeAndAwareness(70, 30);
            setAvailableActions(List.of("Go North", "Use Key", "Use Flashlight"));
        });
    }

    private void initializeUI() {
        // TODO: Implement the UI
        // This is a placeholder

        // Colors
        // Color lightBlack = Color.rgb(8, 8, 8);
        // Color darkGray = Color.rgb(18, 18, 18);
        // Color lightGreen = Color.rgb(4, 194, 25);
        Color darkOrange = Color.rgb(207, 116, 0);
        Color darkPurple = Color.rgb(129, 98, 255);

        stage.setTitle("Unlock - Mafia Escape");
        stage.setMinHeight(300);
        stage.setMinWidth(600);
        // TODO: Check alternative for OnCloseOperation

        // Create the main scene layout
        HBox root = new HBox();
        root.setStyle("-fx-background-color: rgb(18, 18, 18);"); // darkGray

        // Left panel setup
        leftPanel = new BorderPane();
        leftPanel.setPadding(new Insets(10, 10, 10, 10));
        leftPanel.setStyle("-fx-background-color: rgb(18, 18, 18);"); // darkGray

        sceneTextArea = new TextArea();
        sceneTextArea.setEditable(false);
        sceneTextArea.setWrapText(true);
        sceneTextArea.setFocusTraversable(false);
        sceneTextArea.setStyle("-fx-control-inner-background: rgb(8, 8, 8); -fx-text-fill: rgb(4, 194, 25);"); // bg: darkGray, fg: lightGreen
        sceneTextArea.setFont(Font.font("Lucida Console", FontWeight.BOLD, 12));
    
        leftScrollPane = new ScrollPane(sceneTextArea);
        leftScrollPane.setFitToWidth(true);
        leftScrollPane.setFitToHeight(true);
        leftScrollPane.setPrefWidth(300);
        leftScrollPane.setStyle("-fx-background-color: rgb(18, 18, 18); -fx-background: rgb(8, 8, 8); -fx-border-color: rgb(18, 18, 18);"); // TODO: describe color choice

        leftPanel.setCenter(leftScrollPane);

        // Right panel setup
        rightPanel = new BorderPane();
        rightPanel.setPadding(new Insets(10));
        rightPanel.setStyle("-fx-background-color: rgb(18, 18, 18);"); // darkGray

        rightContentPanel = new BorderPane();
        rightContentPanel.setStyle("-fx-background-color: rgb(18, 18, 18);"); // darkGray

        // Status panel (inventory, time, awareness)
        statusPanel = new VBox(3);
        statusPanel.setPadding(new Insets(20, 10, 10, 10));
        statusPanel.setStyle("-fx-background-color: rgb(18, 18, 18);");

        // Inventory label
        inventoryLabel = new Label("Inventory: [Empty]");
        inventoryLabel.setTextFill(darkPurple);
        inventoryLabel.setFont(Font.font("Lucida Console", FontWeight.BOLD, 12));

        // Time remaining label
        timeLabel = new Label("Time Remaining: 70 minutes");
        timeLabel.setTextFill(darkOrange);
        timeLabel.setFont(Font.font("Lucida Console", FontWeight.BOLD, 12));

        // Guard awareness progress bar and label
        awarenessBar = new ProgressBar(0);
        awarenessBar.setPrefWidth(280);
        awarenessBar.setStyle("-fx-accent: rgb(0, 150, 18); -fx-control-inner-background: rgb(38, 38, 38);");

        awarenessLabel = new Label("Guard Awareness: 0%");
        awarenessLabel.setTextFill(Color.WHITESMOKE);
        awarenessLabel.setFont(Font.font("Lucida Console", FontWeight.BOLD, 12));

        statusPanel.getChildren().addAll(inventoryLabel, timeLabel, awarenessBar, awarenessLabel);

        // Action button panel
        actionButtonPanel = new VBox(10);
        actionButtonPanel.setPadding(new Insets(10));
        actionButtonPanel.setStyle("-fx-background-color: rgb(18, 18, 18);"); // darkGray

        // Add componets to right content panel
        rightContentPanel.setTop(statusPanel);
        rightContentPanel.setCenter(new Region()); // Spacer
        rightContentPanel.setBottom(actionButtonPanel);

        // Right scroll pane
        rightScrollPane = new ScrollPane(rightContentPanel);
        rightScrollPane.setFitToWidth(true);
        rightScrollPane.setFitToHeight(true);
        rightScrollPane.setPrefWidth(300);
        rightScrollPane.setStyle("-fx-background-color: rgb(18, 18, 18); -fx-background: rgb(18, 18, 18);"); // darkGray

        rightPanel.setCenter(rightScrollPane);

        // Add panels to root
        root.getChildren().addAll(leftPanel, rightPanel);
        HBox.setHgrow(leftPanel, Priority.ALWAYS);
        HBox.setHgrow(rightPanel, Priority.ALWAYS);

        // Create scene and set stage
        Scene scene = new Scene(root, 600, 300);
        stage.setScene(scene);
    }

    public void display() {
        if (stage != null) stage.show();
    }

    @Override
    public void setSceneText(String text) {
        // JavaFX UI updates must be done on the JavaFX Application Thread
        Platform.runLater(() -> {
            sceneTextArea.setText(text);
            sceneTextArea.positionCaret(0); // Scroll to top
        });
    }

    @SuppressWarnings("unused")
    @Override
    public void setAvailableActions(List<String> actions) {
        // TODO: Clear existing buttons
        // This is a placeholder

        // Colors
        // Color buttonBackground = Color.rgb(19, 53, 112);
        Color buttonForeground = Color.rgb(185, 185, 185);

        Platform.runLater(() -> {
            // Create new buttons for each action
            for (String action : actions) {
                Button button = new Button(action);
                button.setFocusTraversable(true);
                button.setCursor(Cursor.HAND);
                button.setTextFill(buttonForeground);
                button.setStyle("-fx-background-color: rgb(19, 53, 112);"); // buttonBackground
                button.setOnMousePressed(e -> button.setStyle("-fx-background-color: rgb(15, 43, 90);")); // Darker when pressed
                button.setOnMouseReleased(e -> button.setStyle("-fx-background-color: rgb(19, 53, 112);")); // Revert when released
                button.setFont(Font.font("Lucida Console", FontWeight.BOLD, 12));
                button.setPrefWidth(280);
                button.setMaxWidth(Double.MAX_VALUE);
                // TODO: Add action listener to buttons
                // This is a placeholder

                actionButtonPanel.getChildren().add(button);
            }
        });
    }

    @Override
    public void setInventoryText(String text) {
        Platform.runLater(() -> {
            inventoryLabel.setText(text);
        });
    }

    @Override
    public void setTimeAndAwareness(int timeRemaining, int guardAwareness) {
        Platform.runLater(() -> {
            timeLabel.setText("ETA: " + timeRemaining + "min");
            awarenessBar.setProgress(guardAwareness / 100.0);
            awarenessLabel.setText("Guard Awareness: " + guardAwareness + "%");

            // TODO: Set color based on awareness level
            // This is a placeholder
        });
    }

    @Override
    public void showGameOver(boolean isWon) {
        // TODO: Display a game over message
        // This is a placeholder
    }

    public static void main(String[] args) {
        launch(args);
    }
}