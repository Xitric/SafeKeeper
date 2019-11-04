package dk.sdu.privacyenforcer.client;

import java.util.ArrayList;
import java.util.List;

public class ViolationCollection {

    private boolean isAborted;
    private List<PrivacyViolation> violations = new ArrayList<>();

    public boolean addViolation(PrivacyViolation violation) {
        //In the end product, this would also check for conflicts between violations
        return violations.add(violation);
    }

    public void resolveViolations(RequestBody context) {
        //Mutate from the bottom and up to not affect offsets of violations
        for (int i = violations.size() - 1; i >= 0; i--) {
            PrivacyViolation violation = violations.get(i);
            violation.getMutator().mutate(context, violation);
        }
    }

    public void abort() {
        isAborted = true;
    }

    public boolean isAborted() {
        return isAborted;
    }
}
