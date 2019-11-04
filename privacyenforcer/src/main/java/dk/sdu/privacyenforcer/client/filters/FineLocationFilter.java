package dk.sdu.privacyenforcer.client.filters;

import dk.sdu.privacyenforcer.client.mutators.DataMutator;
import dk.sdu.privacyenforcer.client.PrivacyViolation;
import dk.sdu.privacyenforcer.client.RequestBody;
import dk.sdu.privacyenforcer.client.ViolationCollection;

public class FineLocationFilter implements Filter {

    private final DataMutator kAnonymity = new LocationKAnonymityMutator();
    private final DataMutator localObfuscation = new LocationLocalObfuscationMutator();

    @Override
    public void filter(RequestBody context, ViolationCollection violations) {
        //Here we would scan the context for violations to location data
        //Then we would choose a mutator in correspondence to the user's privacy settings

        PrivacyViolation violation1 = new PrivacyViolation(context.getContent().indexOf("Hello"), context.getContent().indexOf("Hello") + "Hello".length(), context, kAnonymity);
        violations.addViolation(violation1);

        PrivacyViolation violation2 = new PrivacyViolation(context.getContent().indexOf("world"), context.getContent().indexOf("world") + "world".length(), context, localObfuscation);
        violations.addViolation(violation2);
    }

    private class LocationKAnonymityMutator implements DataMutator {

        @Override
        public void mutate(RequestBody context, PrivacyViolation violation) {
            context.substitute(violation, "K_ANONYMITY");
        }
    }

    private class LocationLocalObfuscationMutator implements DataMutator {

        @Override
        public void mutate(RequestBody context, PrivacyViolation violation) {
            context.substitute(violation, "LOCAL_OBFUSCATION");
        }
    }
}
