package dk.sdu.privacyenforcer.ui;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import dk.sdu.privacyenforcer.client.repository.LibraryDatabase;
import dk.sdu.privacyenforcer.client.repository.MutatorEntity;

public class SettingsMutatorChoiceFragment extends PreferenceFragmentCompat {

    private static final String BUNDLE_PERMISSION = "bundle_permissionl";

    public static SettingsMutatorChoiceFragment newInstance(String permission) {
        Bundle args = new Bundle();
        args.putString(BUNDLE_PERMISSION, permission);
        SettingsMutatorChoiceFragment fragment = new SettingsMutatorChoiceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        Context context = getPreferenceManager().getContext();
        PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);
        Bundle bundle = getArguments();
        if (bundle != null) {
            LibraryDatabase.getInstance(getContext())
                    .mutatorDAO()
                    .loadAllMutatorsIdsByType(bundle.getString(BUNDLE_PERMISSION))
                    .observe(this, mutatorEntities -> {
                        for (MutatorEntity mutator : mutatorEntities) {
                            Preference mutatorPreference = new Preference(context);
                            mutatorPreference.setKey(mutator.mid);
                            mutatorPreference.setTitle(mutator.mid);
                            mutatorPreference.setSummary(mutator.type);
                            screen.addPreference(mutatorPreference);
                            Log.i("MutatorEntity", "Mid: " + mutator.getMid() + " Type: " + mutator.getType());
                        }

                        setPreferenceScreen(screen);
                    });
        }
    }

}
