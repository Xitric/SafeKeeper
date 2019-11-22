package dk.sdu.privacyenforcer.client;

import java.util.ArrayList;
import java.util.List;

public class ViolationCollection {

    private boolean isAborted;
    private List<PrivacyViolation> violations = new ArrayList<>();

    public boolean addViolation(PrivacyViolation violation) {
        //TODO: In the end product, this would also check for conflicts between violations - what to do on conflicts?
        //TODO: Sort violations so that we can properly apply them in reverse order
        return violations.add(violation);
    }

    public void resolve() {
        //Mutate from the bottom and up to not affect offsets of violations
        for (int i = violations.size() - 1; i >= 0; i--) {
            violations.get(i).resolve();
        }
    }

    public void abort() {
        isAborted = true;
    }

    public boolean isAborted() {
        return isAborted;
    }
}
