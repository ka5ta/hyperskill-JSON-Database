package server;

import java.net.ResponseCache;
import java.util.Arrays;
import java.util.Objects;

public class DataStorage {
    final String[] storage;

    public DataStorage() {
        this.storage = new String[1001];
        Arrays.fill(this.storage, "");
    }


    public String get(int index) {
        try {
            String text = storage[index];
            if ("".equals(text)) {
                return Status.ERROR.name();
            }
            return text;
        } catch (RuntimeException e) {
            return Status.ERROR.name();
        }
    }

    public String save(int index, String text) {
        try {
            storage[index] = text;
            return Status.OK.name();
        } catch (IndexOutOfBoundsException e) {
            return Status.ERROR.name();
        }
    }

    public String delete(int index) {
        try {
            storage[index] = "";
            return Status.OK.name();
        } catch (IndexOutOfBoundsException e) {
            return Status.ERROR.name();
        }
    }



}
