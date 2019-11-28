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

import dk.sdu.privacyenforcer.R;
import dk.sdu.privacyenforcer.client.Privacy;

public class SettingsFragment extends PreferenceFragmentCompat {

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
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
            permissionPreference.setTitle(permission);
            permissionPreference.setKey(permission);
            permissionPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Fragment settingsMutationFragment = SettingsMutationFragment.newInstance(permission);
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, settingsMutationFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    return false;
                }
            });
            screen.addPreference(permissionPreference);
        }
        setPreferenceScreen(screen);
    }
}
