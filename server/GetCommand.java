package server;

public class GetCommand implements Command{

    private int index;

    public GetCommand(int index) {
        this.index = index;
    }

    @Override
    public String execute(DataStorage storage) {
    return storage.get(index);
    }
}
