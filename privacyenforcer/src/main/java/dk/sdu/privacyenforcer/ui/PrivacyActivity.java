package dk.sdu.privacyenforcer.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import dk.sdu.privacyenforcer.PermissionPreferenceUtil;
import dk.sdu.privacyenforcer.client.Privacy;

public class PrivacyActivity extends AppCompatActivity implements SendPermissionsModalFragment.PermissionsModalListener {

    //TODO: Call it mode instead of state or action
    //TODO: Update method names and documentation in PrivacyActivity

    private PermissionPreferenceUtil preferenceUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences(Privacy.PERMISSION_PREFERENCE_FILE, Context.MODE_PRIVATE);
        preferenceUtil = new PermissionPreferenceUtil(sharedPreferences);
    }

    /**
     * Check whether the user has granted access to send the data represented by the specified
     * permission. Specifically, this checks if the mode of the permission is
     * {@link Privacy.Mutation#ALLOW}.
     *
     * @param permission the permission to check for
     * @return {@code true} if the permission is allowed, {@code false} otherwise
     */
    public final boolean checkSendPermission(String permission) {
        return checkSendPermission(permission, Privacy.Mutation.ALLOW);
    }

    /**
     * Check whether the mode of the specified permission matches the specified mode. If no mode has
     * been set for the specified permission, it will implicitly be interpreted as
     * {@link Privacy.Mutation#BLOCK}.
     *
     * @param permission the permission to check for
     * @param mode       the mode to compare with
     * @return {@code true} if the mode of the permission matched the specified mode, {@code false}
     * otherwise
     */
    public final boolean checkSendPermission(String permission, Privacy.Mutation mode) {
        return mode == preferenceUtil.getPermissionMode(permission);
    }

    /**
     * Make a request to the user to configure their preferences regarding the specified
     * permissions.
     *
     * @param permissions  the permissions to request
     * @param explanations strings explaining the intended use of the permissions
     */
    public final void requestSendPermissions(String[] permissions, String[] explanations) {
        DialogFragment newFragment = SendPermissionsModalFragment.newInstance(permissions, explanations);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        newFragment.show(transaction, null);
    }

    /**
     * Called when the user has made a decision regarding a set of permissions, which were requested
     * in {@link #requestSendPermissions(String[], String[])}. If the user cancelled the permissions
     * request, the {@code results} array will be empty.
     *
     * @param permissions the permissions originally requested
     * @param results     the user decisions regarding the requested permissions. One of
     *                    {@link Privacy.Mutation#ALLOW}, {@link Privacy.Mutation#BLOCK} or
     *                    {@link Privacy.Mutation#FAKE}. This array is empty if the user cancelled
     *                    the request
     */
    public void onRequestSendPermissionsResult(String[] permissions, Privacy.Mutation[] results) {
    }

    /**
     * Set the modes of the specified permissions.
     *
     * @param permissions the permissions whose mode to set
     * @param modes       the mutation modes to set
     */
    private void setSendPermissions(String[] permissions, Privacy.Mutation[] modes) {
        preferenceUtil.savePermissions(permissions, modes);
    }

    /**
     * Set the mutator for the permission.
     *
     * @param permission the permission whose mutator to set
     * @param mutator    the mutator for the permission
     */
    private void setMutatorPreference(String permission, String mutator) {
        preferenceUtil.saveMutator(permission, mutator);
    }

    @Override
    public final void onPermissionSelectionCancelled(String[] permissions) {
        onRequestSendPermissionsResult(permissions, new Privacy.Mutation[0]);
    }

    @Override
    public final void onPermissionSelectionResult(String[] permissions, Privacy.Mutation[] modes) {
        setSendPermissions(permissions, modes);
        onRequestSendPermissionsResult(permissions, modes);
    }

    public final void setLocalObfuscationArea() {
        DialogFragment dialogFragment = LocalObfuscationPromptFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        dialogFragment.show(transaction, null);
    }
}
