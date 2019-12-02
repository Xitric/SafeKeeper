package dk.sdu.privacyenforcer.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.HashSet;
import java.util.Set;

import dk.sdu.privacyenforcer.client.Privacy;

public class SettingsFragment extends PreferenceFragmentCompat {

    private static final String FRAGMENT_CONTAINER = "fragment_container";

    public static SettingsFragment newInstance(int fragmentContainerId) {
        SettingsFragment fragment = new SettingsFragment();

        Bundle args = new Bundle();
        args.putInt(FRAGMENT_CONTAINER, fragmentContainerId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        Context context = getPreferenceManager().getContext();
        PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);

        SharedPreferences sharedPreferences = context.getSharedPreferences(Privacy.PERMISSION_PREFERENCE_FILE, Context.MODE_PRIVATE);
        Set<String> permissions = sharedPreferences.getStringSet(Privacy.PERMISSION_PREFERENCES, new HashSet<>());

        for (String permission : permissions) {
            Preference permissionPreference = new Preference(context);
            permissionPreference.setKey(permission);
            permissionPreference.setTitle(PermissionHelper.getPermissionTitle(permission, context));
            permissionPreference.setSummary("Manage your settings for " +
                    PermissionHelper.getPermissionText(permission, context) + " data.");
            permissionPreference.setOnPreferenceClickListener(preference -> {
                onPreferenceAction(permission);
                return false;
            });
            screen.addPreference(permissionPreference);
        }
        setPreferenceScreen(screen);
    }

    private void onPreferenceAction(String permission) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            int fragment_container = bundle.getInt(FRAGMENT_CONTAINER);
            Fragment settingsMutationFragment = SettingsMutationFragment.newInstance(permission, fragment_container);
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(fragment_container, settingsMutationFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

}
