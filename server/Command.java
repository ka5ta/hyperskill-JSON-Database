package server;

public interface Command {
    ResponseDTO execute(DataStorage storage);
}
