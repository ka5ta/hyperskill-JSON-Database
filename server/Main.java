package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    static final String[] storedWords = new String[101];


    public static void main(String[] args) throws IOException {

        Arrays.fill(storedWords, "");

        while (true) {
            String userInput = getUserInput();
            String[] splitInputToWords = transformToWords(userInput);

            //Get Action
            Action action = Action.getAction(splitInputToWords[0]);
            if(action == Action.EXIT){
                return;
            }

            //Set indexFromInput
            Integer indexFromInput = getIndexFromInput(splitInputToWords);
            if(checkIfNull(indexFromInput)) {
                System.out.println(Response.ERROR);
                continue;
            }

            switch (action) {
                case GET:
                    String storedText = getStoredText(storedWords, indexFromInput);
                    if(checkIfNull(storedText)){
                        System.out.println(Response.ERROR);
                        break;
                    }
                    System.out.println(storedText);
                    break;
                case SET:
                    String textToStore = getTextFromInput(splitInputToWords, userInput);
                    if(checkIfNull(textToStore)){
                        System.out.println(Response.ERROR);
                        break;
                    }
                    storeText(indexFromInput, textToStore);
                    break;
                case DELETE:
                    deleteByIndex(indexFromInput);
                    break;
            }
        }
    }

    private static String getUserInput() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }

    private static String[] transformToWords(String input){
        if(Objects.isNull(input)){
            throw new NullPointerException("Input is null.");
        }
        return input.split("\\s+");
    }

    private static Integer getIndexFromInput(String[] words){
        if(words.length < 2) {
            //System.out.println("Index is null.");
            return null;
        }
            String txtNumber =  words[1];
            try{
                return Integer.parseInt(txtNumber);
            } catch (NumberFormatException e) {
                //System.out.println("Index is not a number.");
                return null;
            }
    }

    private static <T> boolean checkIfNull(T value){
        return Objects.isNull(value);
    }

    private static String getStoredText(String[] words, int index){
        try{
            String text = words[index];
            if("".equals(text)){
                return null;
            }
            return text;
        } catch (IndexOutOfBoundsException e){
            return null;
        }
    }

    private static String getTextFromInput(String[] words, String userText){
        if(words.length < 3) {
            return null;
        }

        int start =  userText.indexOf(words[2]);
        return userText.substring(start);
    }

    private static void storeText(int index, String text) {
        try {
            storedWords[index] = text;
            System.out.println(Response.OK);
        } catch (IndexOutOfBoundsException e) {
            System.out.println(Response.ERROR);
        }
    }

    private static void deleteByIndex(int index) {
        try {
            storedWords[index] = "";
            System.out.println(Response.OK);
        } catch (IndexOutOfBoundsException e) {
            System.out.println(Response.ERROR);
        }
    }
}

enum Action {
    GET, SET, DELETE, EXIT;

    public static Action getAction(String command) {
        try {
            return Action.valueOf(command.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Action.EXIT;
        }
    }
}

enum Response {
    ERROR, OK
}



