package dk.sdu.privacyenforcer;

import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

import dk.sdu.privacyenforcer.client.Privacy;

public class PermissionPreferenceUtil {

    private final SharedPreferences sharedPreferences;

    public PermissionPreferenceUtil(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public Set<String> getPermissions() {
        return sharedPreferences.getStringSet(Privacy.PERMISSION_PREFERENCES, new HashSet<>());
    }

    public Privacy.Mutation getPermissionMode(String permission) {
        String storedMode = sharedPreferences.getString(permission + Privacy.MODE_SUFFIX, null);

        if (storedMode == null) {
            return Privacy.Mutation.BLOCK;
        }

        try {
            return Privacy.Mutation.valueOf(storedMode);
        } catch (IllegalArgumentException ignored) {
            //A mode has been configured, but it is somehow unknown
            return Privacy.Mutation.BLOCK;
        }
    }

    public void savePermissions(String[] permissions, Privacy.Mutation[] modes) {
        Set<String> permissionsSet = getPermissions();
        SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();

        for (int i = 0; i < permissions.length; i++) {
            permissionsSet.add(permissions[i]);
            preferenceEditor.putString(permissions[i] + Privacy.MODE_SUFFIX, modes[i].toString());
        }

        preferenceEditor.putStringSet(Privacy.PERMISSION_PREFERENCES, permissionsSet);
        preferenceEditor.apply();
    }

    public String getMutator(String permission) {
        return sharedPreferences.getString(permission + Privacy.MUTATOR_SUFFIX, null);
    }

    public void saveMutator(String permission, String mutator) {
        SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();
        preferenceEditor.putString(permission + Privacy.MUTATOR_SUFFIX, mutator);
        preferenceEditor.apply();
    }
}
