package server;

import com.google.gson.*;
import server.Commands.*;
import server.Enums.Action;
import server.Model.Controller;
import server.Model.ResponseDTO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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
        try (
                DataInputStream dataIn = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream())
        ) {
            //Receive and deserialize message
            String rawRequest = dataIn.readUTF(); // reading a msg
            JsonObject deserializedJsonObject = jsonStringToObject(rawRequest);

            Command command = parseRequest(deserializedJsonObject);
            controller.setCommand(command);
            ResponseDTO result = controller.execute();

            //Serialize response and send
            String response = result.serializeTextResponse();
            dataOut.writeUTF(response); // send msg to the client

            if (controller.isShuttingDown()) {
                this.serverSocket.close();
                this.interrupt();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JsonObject jsonStringToObject(String jsonString) {
        return new GsonBuilder()
                .create()
                .fromJson(jsonString, JsonObject.class);
    }

    public Command parseRequest(JsonObject deserialized) {

        //Get Action
        Action action = Action.getAction(deserialized.get("type").getAsString());
        if (action == Action.EXIT) {
            return new ExitCommand();
        }

        //Get Keys
        List<String> keysList = getKeysList(deserialized);

        switch (action) {
            case GET:
                return new GetCommand(keysList);
            case SET:
                JsonElement value = deserialized.get("value");
                return new SaveCommand(keysList, value);
            case DELETE:
                return new DeleteCommand(keysList);
        }
        throw new RuntimeException("There is no such action");

    }

    private List<String> getKeysList(JsonObject jsonObject) {
        List<String> keysList = new ArrayList<>();
        JsonElement jsonElement = jsonObject.get("key");
        if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            jsonArray.forEach(element -> keysList.add(element.getAsString()));
        } else {
            keysList.add(jsonElement.getAsString());
        }
        return keysList;
    }
}

