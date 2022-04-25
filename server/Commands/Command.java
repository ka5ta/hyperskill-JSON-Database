package server.Commands;

import server.Database.DataStorage;
import server.Model.ResponseDTO;

/**
 * execute() creates response which is sent back to client.
 * The database action is executed based on user request (SAVE, DELETE, GET etc.)
 */

public interface Command {
    ResponseDTO execute(DataStorage jsonDatabase);
}
