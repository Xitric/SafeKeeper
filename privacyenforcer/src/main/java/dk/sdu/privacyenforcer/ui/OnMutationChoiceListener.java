package dk.sdu.privacyenforcer.ui;

import dk.sdu.privacyenforcer.client.Privacy;

public interface OnMutationChoiceListener {

    void onMutationChoiceInteraction(String permission, Privacy.Mutation state);

    void onMutatorChoiceInteraction(String permission, String mutator);
}
