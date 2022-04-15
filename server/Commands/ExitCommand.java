package server.Commands;

import server.Database.DataStorage;
import server.Enums.Status;
import server.Model.ResponseDTO;

public class ExitCommand implements Command {


    @Override
    public ResponseDTO execute(DataStorage jsonDatabase) {
        return new ResponseDTO(Status.OK);
    }
}
