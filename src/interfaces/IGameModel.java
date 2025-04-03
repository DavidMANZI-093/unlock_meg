package interfaces;

import java.util.List;

// Game model interface
public interface IGameModel {
    String getCurrentSceneDescription();
    List<String> getAvailableActions();
    void processAction(String action);
    boolean isGameOver();
    boolean isGameWon();
    String getInventoryString();
    int getTimeRemaining();
    int getGuardAwareness();
}
