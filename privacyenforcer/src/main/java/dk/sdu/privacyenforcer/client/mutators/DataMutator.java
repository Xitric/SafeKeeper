package dk.sdu.privacyenforcer.client.mutators;

import dk.sdu.privacyenforcer.client.PrivacyViolation;
import dk.sdu.privacyenforcer.client.RequestBody;

public interface DataMutator {

    void mutate(RequestBody context, PrivacyViolation violation);
}
