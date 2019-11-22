package dk.sdu.privacyenforcer.client;

import java.util.ArrayList;
import java.util.List;

public class ViolationCollection {

    private boolean isAborted;
    private List<PrivacyViolation> violations = new ArrayList<>();

    public boolean addViolation(PrivacyViolation violation) {
        //TODO: In the end product, this would also check for conflicts between violations - what to do on conflicts?
        return violations.add(violation);
    }

    public void resolve() {
        for (PrivacyViolation violation : violations) {
            violation.resolve();
        }
    }

    public void abort() {
        isAborted = true;
    }

    public boolean isAborted() {
        return isAborted;
    }
}
