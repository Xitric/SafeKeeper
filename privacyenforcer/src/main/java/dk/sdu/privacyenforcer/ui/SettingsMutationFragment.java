package dk.sdu.privacyenforcer.ui;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import dk.sdu.privacyenforcer.client.Privacy;

public class SettingsMutationFragment extends PreferenceFragmentCompat {

    private static final String CHOSEN_PERMISSION = "chosen_permission";
    private static final String FRAGMENT_CONTAINER = "fragment_container";

    private OnMutationChoiceListener listener;

    public static SettingsMutationFragment newInstance(String permission, int fragment_container) {
        Bundle args = new Bundle();
        args.putString(CHOSEN_PERMISSION, permission);
        args.putInt(FRAGMENT_CONTAINER, fragment_container);
        SettingsMutationFragment fragment = new SettingsMutationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnMutationChoiceListener) {
            listener = (OnMutationChoiceListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMutationChoiceListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        Context context = getPreferenceManager().getContext();
        PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);
        Bundle bundle = getArguments();

        for (Privacy.Mutation mutation : Privacy.Mutation.values()) {
            if (bundle != null) {
                String permission = bundle.getString(CHOSEN_PERMISSION);
                Preference mutationPreference = new Preference(context);
                mutationPreference.setKey(mutation.name());
                mutationPreference.setTitle(mutation.name());
                mutationPreference.setSummary("Requests with sensitive "
                        + PermissionHelper.getPermissionText(permission, context) + " data.");
                mutationPreference.setOnPreferenceClickListener(preference -> {
                    onPreferenceAction(permission, preference);
                    return true;
                });
                screen.addPreference(mutationPreference);
            }
        }
        setPreferenceScreen(screen);
    }

    private void onPreferenceAction(String permission, Preference preference) {
        if (preference.getKey().equals(Privacy.Mutation.FAKE.name()) && getArguments() != null) {
            int container = getArguments().getInt(FRAGMENT_CONTAINER);
            Fragment settingsMutatorChoiceFragment = SettingsMutatorChoiceFragment.newInstance(permission);
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(container, settingsMutatorChoiceFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            Privacy.Mutation state = Privacy.Mutation.valueOf(preference.getKey());
            saveMutationPreference(permission, state);
        }
    }

    private void saveMutationPreference(String permission, Privacy.Mutation state) {
        listener.onMutationChoiceInteraction(permission, state);
        String info = "Saved " + state.name() + " setting.";
        Toast.makeText(getContext(), info, Toast.LENGTH_SHORT).show();
    }

}
