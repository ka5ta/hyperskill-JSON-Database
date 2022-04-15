package server.Commands;

import server.Database.DataStorage;
import server.Model.ResponseDTO;

import java.util.List;

public class GetCommand implements Command {

    private final List<String> keys;

    public GetCommand(List<String> keys) {
        this.keys = keys;
    }

    @Override
    public ResponseDTO execute(DataStorage jsonDatabase) {
        return jsonDatabase.get(keys);
    }
}
