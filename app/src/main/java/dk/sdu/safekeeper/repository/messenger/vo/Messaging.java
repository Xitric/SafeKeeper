package dk.sdu.safekeeper.repository.messenger.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Messaging {

    @SerializedName("sender")
    @Expose
    private Sender sender;
    @SerializedName("recipient")
    @Expose
    private Recipient recipient;
    @SerializedName("timestamp")
    @Expose
    private int timestamp;
    @SerializedName("message")
    @Expose
    private Message message;

    public Messaging() {
    }

    public Messaging(Sender sender, Recipient recipient, int timestamp, Message message) {
        super();
        this.sender = sender;
        this.recipient = recipient;
        this.timestamp = timestamp;
        this.message = message;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
