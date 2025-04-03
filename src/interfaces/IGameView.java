package interfaces;

import java.util.List;

// Game view interface
public interface IGameView {
    void setSceneText(String text);
    void setAvailableActions(List<String> actions);
    void setInventoryText(String text);
    void setTimeAndAwareness(int timeRemaining, int guardAwareness);
    void showGameOver(boolean isWon);
}