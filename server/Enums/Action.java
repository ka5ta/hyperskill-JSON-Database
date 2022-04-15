package server.Enums;

public enum Action {
    GET, SET, DELETE, EXIT;

    public static Action getAction(String command) {
        return Action.valueOf(command.toUpperCase());
    }
}

