package dk.sdu.privacyenforcer.ui;

import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.HashSet;
import java.util.Set;

import dk.sdu.privacyenforcer.client.Privacy;

public class PrivacyActivity extends AppCompatActivity implements SendPermissionsModalFragment.PermissionsModalListener,
        LocalObfuscationPromptFragment.OnFragmentInteractionListener {

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
     * interpreted as {@link Privacy.Mutation#BLOCK}.
     *
     * @param permission the permission to check for
     * @param state      the mutation action to compare with
     * @return {@code true} if the state of the permission matched the action, {@code false}
     * otherwise
     */
    public final boolean checkSendPermission(String permission, Privacy.Mutation state) {
        return state == getSendPermissionState(permission);
    }

    /**
     * Get the mutation action configured for the specified permission.
     *
     * @param permission the permission whose mutation action to get
     * @return the mutation action of the specified permission. If no action has been set, this
     * defaults to {@link Privacy.Mutation#BLOCK}
     */
    public final Privacy.Mutation getSendPermissionState(String permission) {
        SharedPreferences preferences = getPermissionPreferences();
        String actionString = preferences.getString(permission, null);

        if (actionString == null) {
            return Privacy.Mutation.BLOCK;
        }

        try {
            return Privacy.Mutation.valueOf(actionString);
        } catch (IllegalArgumentException ignored) {
            //An action has been configured, but it is somehow unknown
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
    public final void requestSendPermissions(String[] permissions, String[] explanations) {
        //TODO: uncomment to stop requesting user input for allowed permissions
//        List<String> permissionsList = new ArrayList<>(Arrays.asList(permissions));
//        List<String> explanationsList = new ArrayList<>(Arrays.asList(explanations));
//
//        filterAllowedPermissions(permissionsList, explanationsList);
//        if (permissionsList.isEmpty()) return;
//
//        permissions = permissionsList.toArray(new String[0]);
//        explanations = explanationsList.toArray(new String[0]);

        DialogFragment newFragment = SendPermissionsModalFragment.newInstance(permissions, explanations);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        newFragment.show(transaction, null);
    }

    //TODO: uncomment to stop requesting user input for allowed permissions
//    private void filterAllowedPermissions(List<String> permissions, List<String> explanations) {
//        for (int i = 0; i < permissions.size(); i++) {
//            if (checkSendPermission(permissions.get(i))) {
//                permissions.remove(i);
//                explanations.remove(i);
//                i--;
//            }
//        }
//    }

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
     * Set the states of the specified permissions.
     *
     * @param permissions the permissions whose state to set
     * @param states      the mutation actions to set
     */
    private void setSendPermissions(String[] permissions, Privacy.Mutation[] states) {
        SharedPreferences preferences = getPermissionPreferences();

        Set<String> permissionsSet = new HashSet<>(preferences.getStringSet(Privacy.PERMISSION_PREFERENCES, new HashSet<>()));
        SharedPreferences.Editor preferenceEditor = preferences.edit();

        for (int i = 0; i < permissions.length; i++) {
            permissionsSet.add(permissions[i]);
            preferenceEditor.putString(permissions[i], states[i].toString());
        }

        preferenceEditor.putStringSet(Privacy.PERMISSION_PREFERENCES, permissionsSet);
        preferenceEditor.apply();
    }

    private SharedPreferences getPermissionPreferences() {
        return getSharedPreferences(Privacy.PERMISSION_PREFERENCE_FILE, MODE_PRIVATE);
    }

    @Override
    public final void onPermissionSelectionCancelled(String[] permissions) {
        onRequestSendPermissionsResult(permissions, new Privacy.Mutation[0]);
    }

    @Override
    public final void onPermissionSelectionResult(String[] permissions, Privacy.Mutation[] states) {
        setSendPermissions(permissions, states);
        onRequestSendPermissionsResult(permissions, states);
    }

    public final void setLocalObfuscationArea() {
        DialogFragment dialogFragment = LocalObfuscationPromptFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        dialogFragment.show(transaction, null);
    }

    @Override
    public void onFragmentInteraction(Location fakeLocation) {
        Log.i("FakeLocation", "Lat: " + fakeLocation.getLatitude() + " Lon: " + fakeLocation.getLongitude());
    }
}
