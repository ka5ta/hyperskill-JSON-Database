package server;

public class ExitCommand implements Command{


    @Override
    public ResponseDTO execute(DataStorage storage) {
        return new ResponseDTO(Status.OK);
    }
}
