package server;

import java.util.Objects;

public class Controller {

    private Command command;
    private DataStorage storage;

    public Controller(DataStorage storage) {
        this.storage = storage;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public String execute() {
        return command.execute(storage);
    }
}


