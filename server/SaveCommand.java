package server;

public class SaveCommand implements Command{


    private int index;
    private String text;

    public SaveCommand(int index, String text) {
        this.index = index;
        this.text = text;
    }

    @Override
    public String execute(DataStorage storage) {
        return storage.save(index, text);
    }
}
