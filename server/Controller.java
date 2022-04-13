package server;

import java.util.Objects;

public class Controller {

    private Command command;
    private DataStorage storage;
    private boolean shuttingDown = false;

    public Controller(DataStorage storage) {
        this.storage = storage;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public ResponseDTO execute() {
        if(command instanceof ExitCommand){
            System.out.println("Shutdown command received. setting shuttingDown flag");
            shuttingDown = true;
        }
        return command.execute(storage);
    }

    public boolean isShuttingDown() {
        return shuttingDown;
    }
}


