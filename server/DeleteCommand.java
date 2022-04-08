package server;

public class DeleteCommand implements Command{

    private int index;

    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public String execute(DataStorage storage) {
        return storage.delete(index);
    }
}
