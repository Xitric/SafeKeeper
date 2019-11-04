package dk.sdu.privacyenforcer.client.filters;

import dk.sdu.privacyenforcer.client.RequestBody;
import dk.sdu.privacyenforcer.client.ViolationCollection;

public class AbortAllFilter implements Filter {

    @Override
    public void filter(RequestBody context, ViolationCollection violations) {
        //A filter for those who are paranoid
        violations.abort();
    }
}
