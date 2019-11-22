package dk.sdu.privacyenforcer.client.mutators;

import dk.sdu.privacyenforcer.client.PrivacyViolation;
import dk.sdu.privacyenforcer.client.RequestContent;

public interface DataMutator {

    void mutate(RequestContent context, PrivacyViolation violation);
}
