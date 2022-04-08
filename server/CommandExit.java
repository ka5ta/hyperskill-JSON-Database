package server;

public class CommandExit implements Command{


    @Override
    public String execute(DataStorage storage) {
        return Status.OK.name();
    }
}
