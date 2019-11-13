package dk.sdu.privacyenforcer.ui;

import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;

public abstract class PrivacyActivity extends AppCompatActivity {

    /**
     * Check whether the user has granted access to send the data represented by the specified
     * permission. Specifically, this checks if the state of the permission is
     * {@link Privacy.Mutation#ALLOW}.
     *
     * @param permission the permission to check for
     * @return {@code true} if the permission is allowed, {@code false} otherwise
     */
    public final boolean checkSendPermission(String permission) {
        return checkSendPermission(permission, Privacy.Mutation.ALLOW);
    }

    /**
     * Check whether the state of the specified permission matches the specified mutation action. If
     * no mutation action has been set for the specified permission, it will implicitly be
     * interpreted as {@link Privacy.Mutation#ALLOW}.
     *
     * @param permission the permission to check for
     * @param state      the mutation action to compare with
     * @return {@code true} if the state of the permission matched the action, {@code false}
     * otherwise
     */
    public final boolean checkSendPermission(String permission, Privacy.Mutation state) {
        SharedPreferences preferences = getPermissionPreferences();
        return state == getSendPermissionState(permission);
    }

    /**
     * Get the mutation action configured for the specified permission.
     *
     * @param permission the permission whose mutation action to get
     * @return the mutation action of the specified permission. If no action has been set, this
     * defaults to {@link Privacy.Mutation#ALLOW}
     */
    public final Privacy.Mutation getSendPermissionState(String permission) {
        SharedPreferences preferences = getPermissionPreferences();
        String actionString = preferences.getString(permission, null);

        if (actionString == null) {
            return Privacy.Mutation.ALLOW;
        }

        try {
            return Privacy.Mutation.valueOf(actionString);
        } catch (IllegalArgumentException ignored) {
            //An action has been configured, but it is somehow unknown. Better block the data to
            //ensure that we do not leak data which the user has made a configuration for
            return Privacy.Mutation.BLOCK;
        }
    }

    /**
     * Make a request to the user to configure their preferences regarding the specified
     * permissions.
     *
     * @param permissions  the permissions to request
     * @param explanations strings explaining the intended use of the permissions
     */
    public void requestSendPermissions(String[] permissions, String[] explanations) {

    }

    /**
     * Called when the user has made a decision regarding a set of permissions, which were requested
     * in {@link #requestSendPermissions(String[], String[])}. If the user cancelled the permissions
     * request, the {@code results} array will be empty.
     *
     * @param permissions the permissions originally requested
     * @param results     the user decisions regarding the requested permissions. One of
     *                    {@link Privacy.Mutation#ALLOW}, {@link Privacy.Mutation#BLOCK} or
     *                    {@link Privacy.Mutation#FAKE}. This array is empty if the user cancelled the request
     */
    public void onRequestSendPermissionsResult(String[] permissions, Privacy.Mutation[] results) {

    }

    /**
     * Set the state of the specified permission.
     *
     * @param permission the permission whose state to set
     * @param state      the mutation action to set
     */
    private void setSendPermission(String permission, Privacy.Mutation state) {
        SharedPreferences preferences = getPermissionPreferences();
        Set<String> permissions = new HashSet<>(preferences.getStringSet(Privacy.PERMISSION_PREFERENCES, new HashSet<String>()));

        SharedPreferences.Editor preferenceEditor = preferences.edit();

        permissions.add(permission);
        preferenceEditor.putStringSet(Privacy.PERMISSION_PREFERENCES, permissions);
        preferenceEditor.putString(permission, state.toString());
        preferenceEditor.apply();
    }

    private SharedPreferences getPermissionPreferences() {
        return getSharedPreferences(Privacy.PERMISSION_PREFERENCE_FILE, MODE_PRIVATE);
    }
}
