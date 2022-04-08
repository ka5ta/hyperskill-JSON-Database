package server;

enum Action {
    GET, SET, DELETE, EXIT;

    public static Action getAction(String command) {
        try {
            return Action.valueOf(command.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Action.EXIT;
        }
    }
}
