package dk.sdu.privacyenforcer.client.filters;


import org.json.JSONObject;

import java.util.ArrayList;

import dk.sdu.privacyenforcer.client.RequestUrl;
import dk.sdu.privacyenforcer.client.ViolationCollection;
import dk.sdu.privacyenforcer.client.mutators.DataMutator;
import dk.sdu.privacyenforcer.ui.Privacy;

public class LocalObfuscationFilter implements Filter {


    @Override
    public void setMode(Privacy.Mutation mode) {

    }

    @Override
    public void filter(RequestUrl requestUrl, ViolationCollection violations) {

    }

    @Override
    public void filter(JSONObject body, ViolationCollection violations) {

    }

    @Override
    public ArrayList<String> getMutatorIdentifiers() {
        return null;
    }
}
