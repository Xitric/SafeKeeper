package dk.sdu.privacyenforcer.client.filters;

import dk.sdu.privacyenforcer.client.RequestBody;
import dk.sdu.privacyenforcer.client.ViolationCollection;

public interface Filter {

    void filter(RequestBody context, ViolationCollection violations);
}
