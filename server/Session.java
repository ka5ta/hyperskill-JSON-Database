package server;

import client.MessageDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Session extends Thread {
    private final Socket socket;
    private final ServerSocket serverSocket;
    private final Controller controller;

    public Session(Socket socket, ServerSocket serverSocket, Controller controller) {
        this.socket = socket;
        this.serverSocket = serverSocket;
        this.controller = controller;
    }

    @Override
    public void run() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit( () -> {
            try (
                    DataInputStream dataIn = new DataInputStream(socket.getInputStream());
                    DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream())
            ) {
                //receive and deserialize message
                String rawRequest = dataIn.readUTF(); // reading a msg
                MessageDTO deserializedMessage = new Gson().fromJson(rawRequest, MessageDTO.class);

                Command command = parseRequest(deserializedMessage);
                controller.setCommand(command);
                ResponseDTO result = controller.execute();

                // serialize response and send
                Gson gsonSerialize = new GsonBuilder()
                        .registerTypeAdapter(ResponseDTO.class, ResponseDTO.serializer)
                        .create();

                String response = gsonSerialize.toJson(result);
                dataOut.writeUTF(response); // send msg to the client

                if(controller.isShuttingDown()){
                    this.serverSocket.close();
                    this.interrupt();
                }

                //socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        executor.shutdown();

    }

    public Command parseRequest(MessageDTO deserialized){

        //Get Action
        Action action = Action.getAction(deserialized.getType());
        if (action == Action.EXIT) {
            return new ExitCommand();
        }

        //Get Index
        String keyFromInput = deserialized.getKey();

        switch (action) {
            case GET:
                return new GetCommand(keyFromInput);
            case SET:
                return new SaveCommand(keyFromInput, deserialized.getValue());
            case DELETE:
                return new DeleteCommand(keyFromInput);
        }
        throw new RuntimeException("Application was terminated. Wrong Command 'null'");
    }

    private static <T> boolean checkIfNull(T value) {
        return Objects.isNull(value);
    }


}

