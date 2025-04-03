package enums;

// Enum for tracking game scenes
public enum Scene {
    CAVE("The Cave"),
    OFFICE("The Capo's Office"),
    EXFILTRATION("The Exfiltration");

    private final String name;

    Scene(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}