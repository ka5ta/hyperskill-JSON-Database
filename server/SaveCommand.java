package server;

public class SaveCommand implements Command{


    private String key;
    private String value;

    public SaveCommand(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public ResponseDTO execute(DataStorage storage) {
        return storage.save(key, value);
    }
}
