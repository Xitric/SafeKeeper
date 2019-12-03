package dk.sdu.privacyenforcer.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import dk.sdu.privacyenforcer.client.Privacy;

@SuppressWarnings("WeakerAccess")
public class SendPermissionsModalViewModel extends ViewModel {

    private MutableLiveData<Integer> currentPage = new MutableLiveData<>(0);
    private MutableLiveData<Boolean> complete = new MutableLiveData<>(false);

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
    }

    LiveData<String> getPermissionName() {
        return Transformations.map(currentPage, i -> permissions[i]);
    }

    LiveData<String> getExplanation() {
        return Transformations.map(currentPage, i -> explanations[i]);
    }

    LiveData<Boolean> isComplete() {
        return complete;
    }

    void grant() {
        assert currentPage.getValue() != null;
        states[currentPage.getValue()] = Privacy.Mutation.ALLOW;
        nextPage();
    }

    void deny() {
        assert currentPage.getValue() != null;
        states[currentPage.getValue()] = Privacy.Mutation.BLOCK;
        nextPage();
    }

    void fake() {
        assert currentPage.getValue() != null;
        states[currentPage.getValue()] = Privacy.Mutation.FAKE;
        nextPage();
    }

    private void nextPage() {
        assert currentPage.getValue() != null;
        if (currentPage.getValue() < permissions.length - 1) {
            currentPage.postValue(currentPage.getValue() + 1);
        } else {
            complete.postValue(true);
        }
    }

    String[] getPermissions() {
        return permissions;
    }

    Privacy.Mutation[] getStates() {
        return states;
    }
}
