package server;

public class DeleteCommand implements Command{

    private String key;

    public DeleteCommand(String key) {
        this.key = key;
    }

    @Override
    public ResponseDTO execute(DataStorage storage) {
        return storage.delete(key);
    }
}
