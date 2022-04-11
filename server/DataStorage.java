package server;

import java.net.ResponseCache;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DataStorage {
    final Map<String, String> storage;

    public DataStorage() {
        this.storage = new HashMap<>(1000);
    }

    public ResponseDTO get(String key) {
        try {
            String returnedKey = storage.get(key);
            if (Objects.isNull(returnedKey)) {
                throw new NullPointerException();
            }
            return new ResponseDTO(Status.OK, returnedKey);
        } catch (NullPointerException e) {
            return new ResponseDTO(Status.ERROR, "No such key");
        }
    }

    public ResponseDTO save(String key, String value) {
        storage.put(key, value);
        return new ResponseDTO(Status.OK);
    }

    public ResponseDTO delete(String key) {
        if (storage.containsKey(key)) {
            storage.remove(key);
            return new ResponseDTO(Status.OK);
        }
        return new ResponseDTO(Status.ERROR, "No such key");
    }
}
