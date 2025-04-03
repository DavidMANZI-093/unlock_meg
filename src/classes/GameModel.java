package classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import enums.Scene;
import interfaces.IGameModel;

public class GameModel implements IGameModel {
    // Game state variables
    private Scene currentScene;
    private List<String> inventory;
    private int timeRemaining;
    private int guardAwareness;

    // Puzzel states
    private boolean isChainCut;
    private boolean isGuardNeutralized;
    private boolean isSafeUnlocked;
    private boolean isPhoneFixed;
    private boolean isEvidenceSent;

    // Scene descriptions
    private Map<Scene, String> sceneDescriptions;
    private Map<Scene, List<String>> sceneActions;

    // Constructor
    public GameModel() {
        // Initialize game state
        currentScene = Scene.CAVE;
        inventory = new ArrayList<>();
        timeRemaining = 70; // 1 + 10 hour
        guardAwareness = 0;

        // Initialize puzzle states
        isChainCut = false;
        isGuardNeutralized = false;
        isSafeUnlocked = false;
        isPhoneFixed = false;
        isEvidenceSent = false;

        // Initialize scene descriptions
        initializeSceneDescriptions();
        initializeSceneActions();
    }

    private void initializeSceneDescriptions() {
        sceneDescriptions = new HashMap<>();
        
        // Initial descriptions for each scene
        sceneDescriptions.put(Scene.CAVE, "You wake up tied to a chair in a dimly lit cellar. Your wrists are bound by a rusty chain locked to the chare. " + "A guard is sleeping nearby with a keychain visibily handing from his belt. " + "You notice a loose board on the floor. the air is damp and you can hear water dripping somewhere.");
        sceneDescriptions.put(Scene.OFFICE, "You've entered the Capo's office. It's an elegant room with dark wood furniture. " + "A large desk dominates the center, and on the wall behind it is a painting that looks slightly askew. " + "A safe is embedded in the wall, hidden behind the painting. " + "On the desk, you see various papers, a pen holder, and a small UV lamp.");
        sceneDescriptions.put(Scene.EXFILTRATION, "You've made it to the back alley. The night air is cool as you hear distant sounds of the city. " + "You see a broken phone in a trash can nearby. This could be your chance to contact the authorities. " + "You can hear voices coming from inside the building. The clock is ticking.");
    }

    private void initializeSceneActions() {
        sceneActions = new HashMap<>();

        // Initialize actions for the CAVE scene
        List<String> caveActions = new ArrayList<>();
        caveActions.add("Examine the chain");
        caveActions.add("Look under the loose board");
        caveActions.add("Check the sleeping guard");
        sceneActions.put(Scene.CAVE, caveActions);

        // Initialize actions for the OFFICE Scene (will be populated when plater reaches this scene)
        sceneActions.put(Scene.OFFICE, new ArrayList<>());

        // Initialize actions for the EXFILTRATION Scene (will be populated when plater reaches this scene)
        sceneActions.put(Scene.EXFILTRATION, new ArrayList<>());
    }

    @Override
    public String getCurrentSceneDescription() {
        return sceneDescriptions.get(currentScene);
    }

    @Override
    public List<String> getAvailableActions() {
        return new ArrayList<>(sceneActions.get(currentScene));
    }

    @Override
    public void processAction(String action) {
        // Decrease time for each action
        timeRemaining -= 5;

        // Process action based on current scene
        switch (currentScene) {
            case CAVE:
                processCaveAction(action);
                break;
            case OFFICE:
                processOfficeAction(action);
                break;
            case EXFILTRATION:
                processExfiltrationAction(action);
                break;
        }

        // Check if we've run out of time
        if (timeRemaining <= 0) {
            sceneDescriptions.put(currentScene, "You hear footsteps approaching. The door swings open, and several armed men enter. You've run out of time.");
            sceneActions.get(currentScene).clear();
        }
    }

    private void processCaveAction(String action) {
        List<String> actions = sceneActions.get(Scene.CAVE);

        switch (action) {
            case "Examine the chain":
                if (!inventory.contains("File")) {
                    sceneDescriptions.put(Scene.CAVE, "The chain is rusty but still strong. You can't break it with your bare hands. " + "You need something to cut it with.");
                } else if (!isChainCut) {
                    sceneDescriptions.put(Scene.CAVE, "Using the file you found, you slowly work on the weakest link of the chain. " + "After several minutes of effort, the chain breaks! Your hands are free now.");
                    isChainCut = true;
                    inventory.remove("File");

                    // Update available actions
                    actions.remove("Examine the chain");
                    if (!isGuardNeutralized) {
                        actions.add("Approach the guard quietly");
                    }
                }
                break;

            case "Look under the loose board":
                if (!inventory.contains("File")) {
                    sceneDescriptions.put(Scene.CAVE, "You carefully lift the loose board, trying not to make any noise. " + "Underneath, you find a rusty metal file. This could be useful.");
                    inventory.add("File");
                } else {
                    sceneDescriptions.put(Scene.CAVE, "You've already taken everything useful from under the board.");
                }
                break;

            case "Check the sleeping guard":
                guardAwareness += 10;
                if (guardAwareness >= 100) {
                    sceneDescriptions.put(Scene.CAVE, "The guard wakes up suddenly! He sees you and raises the alarm. Game over.");
                    actions.clear();
                    return;
                }

                sceneDescriptions.put(Scene.CAVE, "The guard is sleeping soundly. His keychain is hanging from his belt. " + "You can't reach it from here while you're still chained.");
                break;

            case "Approach the guard quietly":
                if (isChainCut && !isGuardNeutralized) {
                    guardAwareness += 20;
                    if (guardAwareness >= 100) {
                        sceneDescriptions.put(Scene.CAVE, "You weren't quiet enough. The guard wakes up, spots you, and raises the alarm. Game over.");
                        actions.clear();
                        return;
                    }

                    sceneDescriptions.put(Scene.CAVE, "You slowly approach the sleeping guard. Carefully, you take a wooden plank from nearby " + "and knock him unconscious with a swift strike. You grab his keychain.");
                    isGuardNeutralized = true;
                    inventory.add("Keys");

                    // Update available actions
                    actions.remove("Approach the guard quietly");
                    actions.remove("Check the sleeping guard");
                    actions.add("Unlock the door and leave");
                }
                break;
            
            case "Unlock the door and leave":
                if (isChainCut && isGuardNeutralized && inventory.contains("Keys")) {
                    sceneDescriptions.put(Scene.CAVE, "Using the guard's keys, you unlock the door and step out into a hallway. " + "You see a door labeled 'Office' at the end of the hall.");

                    //Transition to the OFFICE scene
                    currentScene = Scene.OFFICE;

                    // Transition to the OFFICE scene
                    List<String> officeActions = sceneActions.get(Scene.OFFICE);
                    officeActions.add("Examine the desk");
                    officeActions.add("Look at the painting");
                    officeActions.add("Check the UV lamp");
                }
                break;
        }
    }

    private void processOfficeAction(String action) {
        List<String> actions = sceneActions.get(Scene.OFFICE);
        
        switch (action) {
            case "Examine the desk":
                if (!inventory.contains("Pen")) {
                    sceneDescriptions.put(Scene.OFFICE, "You search through the items on the desk. Among the papers, you find a pen with faded colors. " + "There's something unusual about it. You pocket the pen.");
                    inventory.add("Pen");
                } else {
                    sceneDescriptions.put(Scene.OFFICE, "You've already taken the pen from the desk. The remaining papers don't contain any useful information.");
                }
                break;

            case "Look at the painting":
                sceneDescriptions.put(Scene.OFFICE, "You move the painting aside to reveal a wall safe with a 4-digit keypad. " + "You need the correct code to open it.");

                // Update available actions
                if (!actions.contains("Enter safe code")) {
                    actions.add("Enter safe code");
                }
                break;

            case "Check the UV lamp":
                if (!inventory.contains("UV Lamp")) {
                    sceneDescriptions.put(Scene.OFFICE, "You examine the small UV lamp on the desk. It seems to be working. You pocket it thinking it might be useful.");
                    inventory.add("UV Lamp");
                }

                // Add a new action if you have the UV lamp
                if (!actions.contains("Use UV lamp on note")) {
                    actions.add("Use UV lamp on note");
                } else {
                    sceneDescriptions.put(Scene.OFFICE, "You've already taken the UV lamp.");
                }
                break;

            case "Use UV lamp on note":
                if (inventory.contains("UV Lamp")) {
                    sceneDescriptions.put(Scene.OFFICE, "You shine the UV lamp on a seeming blank note on the desk. Hidden numbers become visible: 1, 5, 9, 3. " + "But in what order should they be entered?");
                    
                    // If you have the pen, provide addition insight
                    if (inventory.contains("Pen")) {
                        sceneDescriptions.put(Scene.OFFICE, sceneDescriptions.get(Scene.OFFICE) + " Looking at the pen more carefully, you notice it has four color bands: yellow, green, red, blue. " + "This might indicate the order of the digits.");
                    }
                }
                break;

            case "Enter safe code":
                if (!isSafeUnlocked) {
                    // The correct code is 9351 (based on the pen's color order: yellow=9, green=3, red=5, blue=1)
                    sceneDescriptions.put(Scene.OFFICE, "You think about the numbers and try to determine the correct order. " + "The pen's colors might be a clue: yellow, green, red, blue. " + "Matching them to the numbers: 9, 3, 5, 1. You enter 9351...");

                    // Success!
                    sceneDescriptions.put(Scene.OFFICE, sceneDescriptions.get(Scene.OFFICE) + " The safe clicks open! Inside, you find a folder labeled 'Operations' containing detailed " + "records of the mafia's illegal activities. This is exactly the evidence you need.");
                    isSafeUnlocked = true;
                    inventory.add("Evidence");

                    // Update availabel actions
                    actions.remove("Enter safe code");
                    actions.add("Leave the office");
                } else {
                    sceneDescriptions.put(Scene.OFFICE, "You've already opened the safe and taken the evidence.");
                }
                break;

            case "Leave the office":
                if (isSafeUnlocked && inventory.contains("Evidence")) {
                    sceneDescriptions.put(Scene.OFFICE, "With the evidence secured, you carefully exit the office and head toward the back alley " + "to find a way to contact the authorities.");

                    // Transition to the EXFILTRATION scene
                    currentScene = Scene.EXFILTRATION;

                    // Set up actions for the EXFILTRATION scene
                    List<String> exfiltrationActions = sceneActions.get(Scene.EXFILTRATION);
                    exfiltrationActions.add("Examine the broken phone");
                    exfiltrationActions.add("Search the trash can");
                    exfiltrationActions.add("Listen for approaching people");
                }
                break;
        }
    }

    private void processExfiltrationAction(String action) {
        List<String> actions = sceneActions.get(Scene.OFFICE);

        switch (action) {
            case "Examine the broken phone":
                sceneDescriptions.put(Scene.EXFILTRATION, "The phone's screen is cracked, but it might still work. However, the battery is missing. " + "You'll need to find a compatible battery.");
                break;

            case "Search the trash can":
                if (!inventory.contains("Battery")) {
                    sceneDescriptions.put(Scene.EXFILTRATION, "You dig through the trash and find a cell phone battery. It looks like it might fit the broken phone.");
                    inventory.add("Battery");
                } else {
                    sceneDescriptions.put(Scene.EXFILTRATION, "You've already searched the trash thoroughly. There's nothing else usefull here.");
                }
                break;

            case "Listen for approaching people":
                guardAwareness += 5;
                if (guardAwareness >= 100) {
                    sceneDescriptions.put(Scene.EXFILTRATION, "You hear footsteps and voices getting closer. Before you can react, several armed men surround you. Game over.");
                    actions.clear();
                    return;
                }

                sceneDescriptions.put(Scene.EXFILTRATION, "You listen carefully. You can hear people moving inside the building, but they don't seem to be coming your way yet. " + "You need to hurry though; it's only a matter of time before they discover you're missing.");
                break;

            case "Fix the phone":
                if (inventory.contains("Battery") && !isPhoneFixed) {
                    sceneDescriptions.put(Scene.EXFILTRATION, "You insert the battery into the phone. The screen lights up! The phone is working, but it needs a PIN code to unlock.");
                    isPhoneFixed = true;

                    // Update available actions
                    actions.remove("Fix the phone");
                    actions.add("Enter phone PIN");
                }
                break;

            case "Enter phone PIN":
                if (isPhoneFixed && !isEvidenceSent) {
                    sceneDescriptions.put(Scene.EXFILTRATION, "You need to guess the PIN. Looking at the evidence documents, you notice a recurring 4-digit number: 1984. " + "You try it as the PIN... It works! The phone unlocks.");

                    // Update available actions
                    actions.remove("Enter phone PIN");
                    actions.add("Send evidence to police");
                }
                break;

            case "Send evidence to police":
                if (isPhoneFixed && inventory.contains("Evidence") && !isEvidenceSent) {
                    sceneDescriptions.put(Scene.EXFILTRATION, "You dial the emergency number and connect with the police. You quickly explain who you are and your situation. " + "You take photos of the evidence documents and send them through a secure channel. " + "The operator confirms receipt and tells you to find a safe hiding spot while they dispatch units to your location. " + "Minutes later, you hear sirens approaching. You've done it! The mafia will be brought to justice thanks to your bravery.");
                    isEvidenceSent = true;

                    // Clear actions since the game is won
                    actions.clear();
                }
                break;
        }

        // Add the fix phone option once we have both the broken phone info and the battery
        if (inventory.contains("Battery") && !isPhoneFixed && !actions.contains("Fix the phone") && sceneDescriptions.get(Scene.EXFILTRATION).contains("battery is missing")) {
            actions.add("Fix the phone");
        }
    }

    @Override
    public boolean isGameOver() {
        return (timeRemaining <= 0 || guardAwareness >= 100 || isEvidenceSent);
    }

    @Override
    public boolean isGameWon() {
        return isEvidenceSent;
    }

    @Override
    public String getInventoryString() {
        if (inventory.isEmpty()) {
            return "Inventory: Empty";
        }
        return "Inventory: " + String.join(", ", inventory);
    }

    @Override
    public int getTimeRemaining() {
        return timeRemaining;
    }

    @Override
    public int getGuardAwareness() {
        return guardAwareness;
    }

    // TODO: Uncomment the section below to access or Modify the Unit test
    // {...}

    // ANSI Color Codes
    // static final String RESET = "\u001B[0m";
    // static final String RED = "\u001B[31m";
    // static final String GREEN = "\u001B[32m";
    // static final String YELLOW = "\u001B[33m";
    // static final String BLUE = "\u001B[34m";
    // static final String PURPLE = "\u001B[35m";
    // static final String CYAN = "\u001B[36m";

    // public static void displayTestData(GameModel testGameModel, int turn) {
    //     // Current scene
    //     System.out.println(RESET + "\n  + Current Scene: " + CYAN + testGameModel.currentScene + RESET);
        
    //     // Data states
    //     System.out.println(RESET + "  |\n  |- Chain cut: " + GREEN + testGameModel.isChainCut + RESET);
    //     System.out.println(RESET + "  |- Guard neutralized: " + GREEN + testGameModel.isGuardNeutralized + RESET);
    //     System.out.println(RESET + "  |- Safe unlocked: " + GREEN + testGameModel.isSafeUnlocked + RESET);
    //     System.out.println(RESET + "  |- Phone fixed: " + GREEN + testGameModel.isPhoneFixed + RESET);
    //     System.out.println(RESET + "  |- Evidence sent: " + GREEN + testGameModel.isEvidenceSent + RESET);
        
    //     // Player's stats
    //     System.out.println("\n  + Guard awareness: " + YELLOW + testGameModel.getGuardAwareness() + "%" + RESET);
    //     System.out.println("  + ETA: " + YELLOW + testGameModel.getTimeRemaining() + " mins" + RESET);

    //     System.out.println("\n  + Inventory: " + CYAN + testGameModel.inventory + RESET);
    //     System.out.println("  + Actions: " + CYAN + testGameModel.getAvailableActions() + RESET);
        
    //     // Game state
    //     System.out.println("\n  + Game over: " + PURPLE + testGameModel.isGameOver() + RESET);
    //     System.out.println("  + Game won: " + PURPLE + testGameModel.isGameWon() + RESET);

    //     System.out.print("\n [" + GREEN + "###".repeat(turn) + RESET + "...".repeat(15 - turn) + "] - " + YELLOW + turn + RESET + " out of " + GREEN + "15" + RESET);
    // }

    // // Method to clear the console screen
    // public static void clearScreen() {
    //     try {
    //         if (System.getProperty("os.name").contains("Windows")) {
    //             new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
    //         } else {
    //             System.out.print("\033[H\033[2J");
    //             System.out.flush();
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

    // // Method to wait for user input or timeout
    // public static void waitForUser() {
        
    //     // System.out.println("\nWait 3 seconds...");
    //     try {
    //         Thread.sleep(2000); // 3-sec timeout
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

    // public static void main(String[] args) {
    //     try {
    //         clearScreen();
    //         System.out.println("Test Sequence - 001: Full and Successfull GamePlay");
            
    //         // Constructing test instance
    //         GameModel testGameModel = new GameModel();

    //         // Model stage
    //         System.out.println(BLUE + "\n  RUNNING: " + YELLOW + "GameModel " + BLUE + "testGameModel" + RESET + "=" + CYAN + " new" + YELLOW + " GameModel" + PURPLE + "()" + RESET + ";");
    //         displayTestData(testGameModel, 1);
    //         waitForUser();

    //         // Game flow
    //         testGameModel.processAction("Look under the loose board");
    //         clearScreen();
    //         System.out.println("\n Test Sequence - " + PURPLE + "001" + RESET + ": Full and Successfull GamePlay");
    //         // Model stage
    //         System.out.println(BLUE + "\n  RUNNING: " + RESET + "testGameModel." + YELLOW + "processAction" + PURPLE + "(" + RESET + "\"Look under the loose board\"" + PURPLE + ")" + RESET + ";");
    //         displayTestData(testGameModel, 2);
    //         waitForUser();

    //         testGameModel.processAction("Examine the chain");
    //         clearScreen();
    //         System.out.println("\n Test Sequence - " + PURPLE + "001" + RESET + ": Full and Successfull GamePlay");
    //         // Model stage
    //         System.out.println(BLUE + "\n  RUNNING: " + RESET + "testGameModel." + YELLOW + "processAction" + PURPLE + "(" + RESET + "\"Examine the chain\"" + PURPLE + ")" + RESET + ";");
    //         displayTestData(testGameModel, 3);
    //         waitForUser();

    //         testGameModel.processAction("Approach the guard quietly");
    //         clearScreen();
    //         System.out.println("\n Test Sequence - " + PURPLE + "001" + RESET + ": Full and Successfull GamePlay");
    //         // Model stage
    //         System.out.println(BLUE + "\n  RUNNING: " + RESET + "testGameModel." + YELLOW + "processAction" + PURPLE + "(" + RESET + "\"Approach the guard quietly\"" + PURPLE + ")" + RESET + ";");
    //         displayTestData(testGameModel, 4);
    //         waitForUser();

    //         testGameModel.processAction("Unlock the door and leave");
    //         clearScreen();
    //         System.out.println("\n Test Sequence - " + PURPLE + "001" + RESET + ": Full and Successfull GamePlay");
    //         // Model stage
    //         System.out.println(BLUE + "\n  RUNNING: " + RESET + "testGameModel." + YELLOW + "processAction" + PURPLE + "(" + RESET + "\"Unlock the door and leave\"" + PURPLE + ")" + RESET + ";");
    //         displayTestData(testGameModel, 5);
    //         waitForUser();

    //         testGameModel.processAction("Examine the desk");
    //         clearScreen();
    //         System.out.println("\n Test Sequence - " + PURPLE + "001" + RESET + ": Full and Successfull GamePlay");
    //         // Model stage
    //         System.out.println(BLUE + "\n  RUNNING: " + RESET + "testGameModel." + YELLOW + "processAction" + PURPLE + "(" + RESET + "\"Examine the desk\"" + PURPLE + ")" + RESET + ";");
    //         displayTestData(testGameModel, 6);
    //         waitForUser();

    //         testGameModel.processAction("Check the UV lamp");
    //         clearScreen();
    //         System.out.println("\n Test Sequence - " + PURPLE + "001" + RESET + ": Full and Successfull GamePlay");
    //         // Model stage
    //         System.out.println(BLUE + "\n  RUNNING: " + RESET + "testGameModel." + YELLOW + "processAction" + PURPLE + "(" + RESET + "\"Check the UV lamp\"" + PURPLE + ")" + RESET + ";");
    //         displayTestData(testGameModel, 7);
    //         waitForUser();

    //         testGameModel.processAction("Use UV lamp on note");
    //         clearScreen();
    //         System.out.println("\n Test Sequence - " + PURPLE + "001" + RESET + ": Full and Successfull GamePlay");
    //         // Model stage
    //         System.out.println(BLUE + "\n  RUNNING: " + RESET + "testGameModel." + YELLOW + "processAction" + PURPLE + "(" + RESET + "\"Use UV lamp on note\"" + PURPLE + ")" + RESET + ";");
    //         displayTestData(testGameModel, 8);
    //         waitForUser();

    //         testGameModel.processAction("Look at the painting");
    //         clearScreen();
    //         System.out.println("\n Test Sequence - " + PURPLE + "001" + RESET + ": Full and Successfull GamePlay");
    //         // Model stage
    //         System.out.println(BLUE + "\n  RUNNING: " + RESET + "testGameModel." + YELLOW + "processAction" + PURPLE + "(" + RESET + "\"Look at the painting\"" + PURPLE + ")" + RESET + ";");
    //         displayTestData(testGameModel, 9);
    //         waitForUser();

    //         testGameModel.processAction("Enter safe code");
    //         clearScreen();
    //         System.out.println("\n Test Sequence - " + PURPLE + "001" + RESET + ": Full and Successfull GamePlay");
    //         // Model stage
    //         System.out.println(BLUE + "\n  RUNNING: " + RESET + "testGameModel." + YELLOW + "processAction" + PURPLE + "(" + RESET + "\"Enter safe code\"" + PURPLE + ")" + RESET + ";");
    //         displayTestData(testGameModel, 10);
    //         waitForUser();

    //         testGameModel.processAction("Leave the office");
    //         clearScreen();
    //         System.out.println("\n Test Sequence - " + PURPLE + "001" + RESET + ": Full and Successfull GamePlay");
    //         // Model stage
    //         System.out.println(BLUE + "\n  RUNNING: " + RESET + "testGameModel." + YELLOW + "processAction" + PURPLE + "(" + RESET + "\"Leave the office\"" + PURPLE + ")" + RESET + ";");
    //         displayTestData(testGameModel, 11);
    //         waitForUser();

    //         testGameModel.processAction("Search the trash can");
    //         clearScreen();
    //         System.out.println("\n Test Sequence - " + PURPLE + "001" + RESET + ": Full and Successfull GamePlay");
    //         // Model stage
    //         System.out.println(BLUE + "\n  RUNNING: " + RESET + "testGameModel." + YELLOW + "processAction" + PURPLE + "(" + RESET + "\"Search the trash can\"" + PURPLE + ")" + RESET + ";");
    //         displayTestData(testGameModel, 12);
    //         waitForUser();

    //         testGameModel.processAction("Fix the phone");
    //         clearScreen();
    //         System.out.println("\n Test Sequence - " + PURPLE + "001" + RESET + ": Full and Successfull GamePlay");
    //         // Model stage
    //         System.out.println(BLUE + "\n  RUNNING: " + RESET + "testGameModel." + YELLOW + "processAction" + PURPLE + "(" + RESET + "\"Fix the phone\"" + PURPLE + ")" + RESET + ";");
    //         displayTestData(testGameModel, 13);
    //         waitForUser();

    //         testGameModel.processAction("Enter phone PIN");
    //         clearScreen();
    //         System.out.println("\n Test Sequence - " + PURPLE + "001" + RESET + ": Full and Successfull GamePlay");
    //         // Model stage
    //         System.out.println(BLUE + "\n  RUNNING: " + RESET + "testGameModel." + YELLOW + "processAction" + PURPLE + "(" + RESET + "\"Enter phone PIN\"" + PURPLE + ")" + RESET + ";");
    //         displayTestData(testGameModel, 14);
    //         waitForUser();

    //         testGameModel.processAction("Send evidence to police");
    //         clearScreen();
    //         System.out.println("\n Test Sequence - " + PURPLE + "001" + RESET + ": Full and Successfull GamePlay");
    //         // Model stage
    //         System.out.println(BLUE + "\n  RUNNING: " + RESET + "testGameModel." + YELLOW + "processAction" + PURPLE + "(" + RESET + "\"Send evidence to police\"" + PURPLE + ")" + RESET + ";");
    //         displayTestData(testGameModel, 15);
    //         waitForUser();

    //         System.out.println(" - [" + GREEN + "Test Completed" + RESET + "]");
    //     } catch (Exception e) {
    //         System.err.println("[Test Failed]\n" + e.getStackTrace());
    //     }
          
    // }
}

