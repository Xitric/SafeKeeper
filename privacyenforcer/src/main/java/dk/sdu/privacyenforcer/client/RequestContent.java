package dk.sdu.privacyenforcer.client;

public class RequestContent {

    private String content;

    public RequestContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getContent(int beginOffset, int endOffset) {
        return content.substring(beginOffset, endOffset);
    }

    public void substitute(int beginOffset, int endOffset, String newContent) {
        content = content.substring(0, beginOffset)
                + newContent
                + content.substring(endOffset);
    }
}
