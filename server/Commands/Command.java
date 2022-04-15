package server.Commands;

import server.Database.DataStorage;
import server.Model.ResponseDTO;

public interface Command {
    ResponseDTO execute(DataStorage jsonDatabase);
}
