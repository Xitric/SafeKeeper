package dk.sdu.privacyenforcer.ui;


import android.content.Context;
import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.Arrays;

public class SettingsFragment extends PreferenceFragmentCompat {

    private String[] permissions = new String[]{
            Privacy.Permission.SEND_LOCATION,
            Privacy.Permission.SEND_ACCELERATION,
            Privacy.Permission.SEND_CONTACTS
    };

    private String[] mutations = new String[]{
          "Allow", "Block", "Fake"
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        Context context = getPreferenceManager().getContext();
        PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);

        for (String permission : permissions) {
            ListPreference listPreference = new ListPreference(context);
            listPreference.setEntries(mutations);
            listPreference.setEntryValues(mutations);
            listPreference.setKey(permission);
            listPreference.setTitle(permission);
            listPreference.setSummary("Steal your info");
            screen.addPreference(listPreference);
        }

        setPreferenceScreen(screen);
    }

}
