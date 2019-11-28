package dk.sdu.safekeeper.repository.messenger.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Message {

    @SerializedName("mid")
    @Expose
    private String mid;
    @SerializedName("seq")
    @Expose
    private int seq;
    @SerializedName("attachments")
    @Expose
    private List<Attachment> attachments = null;

    public Message() {
    }

    public Message(String mid, int seq, List<Attachment> attachments) {
        super();
        this.mid = mid;
        this.seq = seq;
        this.attachments = attachments;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }
}
