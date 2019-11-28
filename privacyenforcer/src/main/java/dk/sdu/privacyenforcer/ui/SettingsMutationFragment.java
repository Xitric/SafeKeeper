package dk.sdu.privacyenforcer.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

public class SettingsMutationFragment extends PreferenceFragmentCompat {

    private static final String BUNDLE_PERMISSION = "bundle_permission";
    private static final String FRAGMENT_CONTAINER = "fragment_container";

    public static SettingsMutationFragment newInstance(String permission, int fragment_container) {
        Bundle args = new Bundle();
        args.putString(BUNDLE_PERMISSION, permission);
        args.putInt(FRAGMENT_CONTAINER, fragment_container);
        SettingsMutationFragment fragment = new SettingsMutationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        Context context = getPreferenceManager().getContext();
        PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);

        Bundle bundle = getArguments();
        String permission = "";
        if (bundle != null) {
            permission = bundle.getString(BUNDLE_PERMISSION);
            screen.setTitle(permission);
        }

        Preference allow = new Preference(context);
        allow.setKey("ALLOW");
        allow.setTitle("ALLOW");
        allow.setSummary(permission);
        allow.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // TODO handle block choice
                // Save it in shared preferences?
                // Go back to SettingsFragment afterwards.
                return false;
            }
        });

        Preference block = new Preference(context);
        block.setKey("BLOCK");
        block.setTitle("BLOCK");
        block.setSummary(permission);
        block.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // TODO handle block choice
                // Save it in shared preferences?
                // Go back to SettingsFragment afterwards.
                return false;
            }
        });

        Preference fake = new Preference(context);
        fake.setKey("FAKE");
        fake.setTitle("FAKE");
        fake.setSummary(permission);
        String finalPermission = permission;
        fake.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Bundle args = new Bundle();
                int container = args.getInt(FRAGMENT_CONTAINER);
                Fragment settingsMutatorChoiceFragment = SettingsMutatorChoiceFragment.newInstance(finalPermission);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(container, settingsMutatorChoiceFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                return false;
            }
        });
        screen.addPreference(allow);
        screen.addPreference(block);
        screen.addPreference(fake);
        setPreferenceScreen(screen);
    }
}
