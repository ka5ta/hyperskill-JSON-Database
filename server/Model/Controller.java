package server.Model;

import server.Commands.Command;
import server.Commands.ExitCommand;
import server.Database.DataStorage;

public class Controller {

    private Command command;
    private final DataStorage jsonDatabase;
    private boolean shuttingDown = false;

    public Controller(DataStorage jsonDatabase) {
        this.jsonDatabase = jsonDatabase;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public ResponseDTO execute() {
        if(command instanceof ExitCommand){
            shuttingDown = true;
        }
        return command.execute(jsonDatabase);
    }

    public boolean isShuttingDown() {
        return shuttingDown;
    }
}


