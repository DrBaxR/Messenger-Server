package messenger.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.*;

@Entity
public class Message {

    @Id
    @GeneratedValue
    private String id;

    private String text;
    private String date;
    private String sender;

    public Message(String text, String date, String sender) {

        this.text = text;
        this.date = date;
        this.sender = sender;
    }

    public Message() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    @Override
    public String toString() {
        return "Message{" +
                "text='" + text + '\'' +
                ", date='" + date + '\'' +
                ", sender='" + sender + '\'' +
                '}';
    }
}
