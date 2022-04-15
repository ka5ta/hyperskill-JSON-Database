package server.Commands;

import com.google.gson.JsonElement;
import server.Database.DataStorage;
import server.Model.ResponseDTO;

import java.util.List;

public class SaveCommand implements Command {

    private final List<String> keys;
    private final JsonElement value;

    public SaveCommand(List<String> keys, JsonElement value) {
        this.keys = keys;
        this.value = value;
    }

    @Override
    public ResponseDTO execute(DataStorage jsonDatabase) {
        return jsonDatabase.save(keys, value);
    }
}
