package server.Commands;

import server.Database.DataStorage;
import server.Model.ResponseDTO;

import java.util.List;

public class DeleteCommand implements Command {

    private final List<String> key;

    public DeleteCommand(List<String> key) {
        this.key = key;
    }

    @Override
    public ResponseDTO execute(DataStorage jsonDatabase) {
        return jsonDatabase.delete(key);
    }
}
