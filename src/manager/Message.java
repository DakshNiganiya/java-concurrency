package manager;

import java.util.Objects;

public class Message {
    private final int id;
    private final String data;

    public Message(int id, String data) {
        this.id = id;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return getId() == message.getId() && Objects.equals(getData(), message.getData());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getData());
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", data='" + data + '\'' +
                '}';
    }
}
