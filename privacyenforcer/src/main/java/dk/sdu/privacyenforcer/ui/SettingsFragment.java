package dk.sdu.privacyenforcer.ui;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.HashSet;
import java.util.Set;

public class SettingsFragment extends PreferenceFragmentCompat {

//    private String[] permissions = new String[]{
//            Privacy.Permission.SEND_LOCATION,
//            Privacy.Permission.SEND_ACCELERATION,
//            Privacy.Permission.SEND_CONTACTS
//    };

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
        SharedPreferences sharedPreferences = context.getSharedPreferences(Privacy.PERMISSION_PREFERENCE_FILE, Context.MODE_PRIVATE);
        Set<String> permissions = sharedPreferences.getStringSet(Privacy.PERMISSION_PREFERENCES, new HashSet<>());

        PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);

        assert permissions != null;
        for (String permission : permissions) {
            ListPreference listPreference = new ListPreference(context);
            listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    checkPreferenceValue(preference, newValue);
                    return true;
                }
            });
            listPreference.setEntries(mutations);
            listPreference.setEntryValues(mutations);
            listPreference.setKey(permission);
            listPreference.setTitle(permission);
            listPreference.setSummary("Steal your info");
            screen.addPreference(listPreference);
        }

        setPreferenceScreen(screen);
    }

    private void checkPreferenceValue(Preference preference, Object newValue) {
        if (newValue.equals("Fake")) {
            Log.i("PreferenceUI", "Value is fake" + newValue.toString());
            DialogFragment newFragment = SendPermissionsModalFragment.newInstance(new String[]{"hej", "med"}, new String[]{"hej2", "hej3"});
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            newFragment.show(transaction, null);

        } else {
            Log.i("PreferenceUI", "" + newValue.toString());
        }
    }


}
