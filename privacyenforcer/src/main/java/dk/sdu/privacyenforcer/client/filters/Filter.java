package dk.sdu.privacyenforcer.client.filters;

import dk.sdu.privacyenforcer.client.RequestContent;
import dk.sdu.privacyenforcer.client.ViolationCollection;
import dk.sdu.privacyenforcer.ui.Privacy;

public interface Filter {

    void setMode(Privacy.Mutation mode);

    void filter(RequestContent context, ViolationCollection violations);
}
