package server;

public class GetCommand implements Command{

    private String key;

    public GetCommand(String key) {
        this.key = key;
    }

    @Override
    public ResponseDTO execute(DataStorage storage) {
    return storage.get(key);
    }
}
