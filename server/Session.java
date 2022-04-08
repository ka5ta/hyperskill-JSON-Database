package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

class Session extends Thread {
    private final Socket socket;
    private final Controller controller;

    public Session(Socket socket, Controller controller) {
        this.socket = socket;
        this.controller = controller;
    }

    @Override
    public void run() {
        try (
                DataInputStream dataIn = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream())
        ) {
            String rawRequest = dataIn.readUTF(); // reading a msg

            Command command = parseRequest(rawRequest);
            controller.setCommand(command);
            String result = controller.execute();

            dataOut.writeUTF(result); // send msg to the client
            //socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Command parseRequest(String rawRequest){
        String[] splitInputToWords = transformToWords(rawRequest);

        //Get Action
        Action action = Action.getAction(splitInputToWords[0]);
        if (action == Action.EXIT) {
            return new CommandExit();
        }

        //Get index From Input
        Integer indexFromInput = getIndexFromInput(splitInputToWords);
        if (checkIfNull(indexFromInput)) {
            throw new RuntimeException("Application was terminated. Index is 'null'");
        }

        switch (action) {
            case GET:
                return new GetCommand(indexFromInput);
            case SET:
                String messageToSent = getTextFromInput(splitInputToWords, rawRequest);
                return new SaveCommand(indexFromInput, messageToSent);
            case DELETE:
                return new DeleteCommand(indexFromInput);
        }
        throw new RuntimeException("Application was terminated. Wrong Command 'null'");
    }

    private static String[] transformToWords(String input) {
        if (Objects.isNull(input)) {
            throw new NullPointerException("Input is null.");
        }
        return input.split("\\s+");
    }

    private static Integer getIndexFromInput(String[] words) {
        if (words.length < 2) {
            return null;
        }
        String txtNumber = words[1];
        try {
            return Integer.parseInt(txtNumber);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static <T> boolean checkIfNull(T value) {
        return Objects.isNull(value);
    }

    private static String getTextFromInput (String[]words, String userText){
        if (words.length < 3) {
            return null;
        }
        int start = userText.indexOf(words[2]);
        return userText.substring(start);
    }
}

