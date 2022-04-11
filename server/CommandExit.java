package server;

public class CommandExit implements Command{


    @Override
    public ResponseDTO execute(DataStorage storage) {
        return new ResponseDTO(Status.OK);
    }
}
