package dk.sdu.privacyenforcer.client;

public class RequestBody {

    private String content;

    public RequestBody(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getContent(PrivacyViolation violation) {
        return content.substring(violation.getBeginOffset(), violation.getEndOffset());
    }

    public void substitute(PrivacyViolation violation, String newContent) {
        content = content.substring(0, violation.getBeginOffset())
                + newContent
                + content.substring(violation.getEndOffset());
    }
}
