# Debrief on building the "Unlock - Mafia Escape" Game

This document provides an overview of the "Mafia Escape" text-based adventure game, implemented using the **Model-View-Controller (MVC)** pattern, to assist our team in building the game’s codebase. Below, we detail the game’s structure, key components, their interactions, and guidelines for the building process.

---

## 1. Introduction to MVC in "Unlock - Mafia Escape"

The **MVC pattern** divides the application into three interconnected components:

- **Model**: Manages the game’s data and logic (e.g., scenes, inventory, time, puzzles).
- **View**: Handles the user interface, displaying the game state and capturing player inputs.
- **Controller**: Coordinates between the model and view, ensuring the UI reflects the game state and processing actions.

This separation enhances modularity, making the code easier to maintain and extend.

---

## 2. Overview of the Code Plan

The codebase is organized into three primary layers, plus a main application class:

### Model (`GameModel`)

- **Purpose**: Implements the game’s logic and state management.
- **Key Features**:
    - **State Variables**: Tracks the current scene (`Scene` enum: `CAVE`, `OFFICE`, `EXFILTRATION`), inventory (list of items), time remaining (minutes), guard awareness (0-100), and puzzle states (e.g., `isChainCut`, `isSafeUnlocked`).
    - **Data Structures**: Uses `Map<Scene, String>` for scene descriptions and `Map<Scene, List<String>>` for available actions.
    - **Logic**: The `processAction` method handles player actions per scene, updating the state and checking game-over conditions (time runs out, guard awareness reaches 100, or evidence is sent).
- **Example**: In the `CAVE` scene, cutting the chain with a file updates `isChainCut` and adjusts available actions.

### View (`GameView`)

- **Purpose**: Renders the UI using Swing.
- **Key Features**:
    - **Components**:
        - `JTextArea` for scene descriptions.
        - `JPanel` with dynamically created `JButton`s for actions.
        - `JLabel`s for inventory and time, and a `JProgressBar` for guard awareness.
    - **Behavior**: Updates the UI based on model data but contains no game logic.
- **Example**: Action buttons are regenerated each time `setAvailableActions` is called, reflecting the model’s current state.

### Controller (`GameController`)

- **Purpose**: Mediates between the model and view.
- **Key Features**:
    - **Initialization**: Starts the game by syncing the view with the model’s initial state.
    - **Action Handling**: Processes player actions via `onActionSelected`, updating the model and view.
    - **Game Flow**: Checks for game-over conditions and triggers the appropriate UI response.
- **Example**: After an action, `updateView` refreshes the UI with the latest model data.

### Main Application (`MafiaEscapeGame`)

- **Purpose**: Entry point for the application.
- **Key Features**: Instantiates the model, view, and controller, and starts the game on the Swing Event Dispatch Thread (EDT) for thread safety.

---

## 3. Key Interactions

The components interact as follows:

1. **Game Start**: The controller calls `startGame()`, invoking `updateView()` to display the initial state (e.g., `CAVE` scene).
2. **Player Input**: The view captures a button click (e.g., "Examine the chain") and notifies the controller via `onActionSelected`.
3. **Action Processing**: The controller instructs the model to `processAction`, which updates the game state (e.g., adding "File" to inventory).
4. **UI Update**: The controller calls `updateView`, refreshing the view with the new scene description, actions, inventory, time, and awareness.
5. **Game End**: If `isGameOver()` returns true, the controller triggers `showGameOver` in the view, displaying a win/loss message.

This flow ensures each component focuses on its role, maintaining clean separation.

---

## 4. Building Guidelines

To build "Unlock - Mafia Escape" effectively, We will follow these recommendations:

### Maintain Separation of Concerns

- **Model**: We will keep things focused on game logic—avoiding UI code (e.g., no Swing imports).
- **View**: We will limit ourselves to presentation and input capture—no game logic (e.g., don’t process actions here).
- **Controller**: We will handle coordination only—don’t duplicate model or view responsibilities.

### Extendibility

- **Design Patterns**: Consider a Factory pattern for scene creation or a Strategy pattern for action handling if complexity grows.

### UI Improvements

- **Thread Safety**: Ensure all UI updates occur on the EDT using `SwingUtilities.invokeLater`.

### Testing

- **Model Tests**: We will write unit tests for `GameModel` to verify logic (e.g., `processAction` outcomes).
- **Integration Tests**: We will also test the controller to ensure proper model-view synchronization.

### Potential Enhancements

- **Localization**: We will externalize strings (e.g., scene descriptions) into resource files.
- **Accessibility**: We might consider enhancing the UI with keyboard navigation.

---

## 5. Specific Code Insights

- **Enums (`Scene`)**: Provide type safety and readability for scene management.
- **Dynamic Actions**: The view’s button panel adapts to the model’s `getAvailableActions` output.
- **`updateView` Method**: Central to UI consistency, called after every action or game start.
- **Progress Bar**: Visually indicates guard awareness with color changes (green <50, yellow <75, red ≥75).

---

## 6. Conclusion

"Mafia Escape" will be built on a solid MVC foundation, offering a clear structure for our building process. By preserving this separation, enhancing the UI, and extending functionality, our team can create an improved version while maintaining maintainability. Leverage the existing patterns and consider any suggested enhancements to deliver a robust, engaging game.

Feel free to reach out for further clarification or assistance during the building process!