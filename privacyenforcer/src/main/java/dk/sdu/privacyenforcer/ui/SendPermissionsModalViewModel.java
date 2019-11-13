package dk.sdu.privacyenforcer.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Arrays;

@SuppressWarnings("WeakerAccess")
public class SendPermissionsModalViewModel extends ViewModel {

    private MutableLiveData<String> currentPermissionName = new MutableLiveData<>();
    private MutableLiveData<String> currentExplanation = new MutableLiveData<>();

    private String[] permissions;
    private String[] explanations;
    private Privacy.Mutation[] states;

    void init(String[] permissions, String[] explanations) {
        if (this.permissions != null && this.explanations != null) {
            return;
        }

        this.permissions = permissions;
        this.explanations = explanations;
        this.states = new Privacy.Mutation[permissions.length];

        //TODO: Remove
        Arrays.fill(this.states, Privacy.Mutation.ALLOW);
    }

    LiveData<String> getPermissionName() {
        return currentPermissionName;
    }

    LiveData<String> getExplanation() {
        return currentExplanation;
    }

    String[] getPermissions() {
        return permissions;
    }

    Privacy.Mutation[] getStates() {
        return states;
    }
}
