package client;

public class MessageDTO {
    private String type;
    private String key;
    private String value;

    public MessageDTO(String type) {
        this.type = type;
    }

    public MessageDTO(String type, String key) {
        this(type);
        this.key = key;
    }

    public MessageDTO(String type, String key, String value) {
        this(type, key);
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "action=" + type +
                ", index=" + key +
                ", text='" + value + '\'' +
                '}';
    }
}
