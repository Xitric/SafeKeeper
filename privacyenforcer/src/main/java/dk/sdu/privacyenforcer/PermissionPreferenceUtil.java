package dk.sdu.privacyenforcer;

import android.content.Context;
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
        } else {
            return Privacy.Mutation.valueOf(storedMode);
        }
    }
}
