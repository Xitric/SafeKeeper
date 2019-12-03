package dk.sdu.privacyenforcer.ui;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.List;

import dk.sdu.privacyenforcer.client.Privacy;
import dk.sdu.privacyenforcer.client.repository.LibraryDatabase;
import dk.sdu.privacyenforcer.client.repository.MutatorEntity;

public class SettingsMutatorChoiceFragment extends PreferenceFragmentCompat {

    private static final String BUNDLE_PERMISSION = "bundle_permissionl";

    private OnMutationChoiceListener listener;

    public static SettingsMutatorChoiceFragment newInstance(String permission) {
        Bundle args = new Bundle();
        args.putString(BUNDLE_PERMISSION, permission);
        SettingsMutatorChoiceFragment fragment = new SettingsMutatorChoiceFragment();
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
        Bundle bundle = getArguments();
        if (bundle != null) {
            String permission = bundle.getString(BUNDLE_PERMISSION);
            LiveData<List<MutatorEntity>> mutatorEntities = fetchFromDatabase(permission);
            mutatorEntities.observe(this, this::createPreferenceScreen);
        }
    }

    private LiveData<List<MutatorEntity>> fetchFromDatabase(String permission) {
        return LibraryDatabase.getInstance(getContext()).mutatorDAO().loadAllMutatorsIdsByType(permission);
    }

    private void createPreferenceScreen(List<MutatorEntity> mutatorEntities) {
        Context context = getPreferenceManager().getContext();
        PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);

        for (MutatorEntity mutatorEntity : mutatorEntities) {
            Preference mutatorPreference = new Preference(context);

            String permission = mutatorEntity.type;
            String mutator = mutatorEntity.mid;
            String title = PermissionHelper.getPermissionTitle(mutator, context);
            String permissionType = PermissionHelper.getPermissionText(permission, context);
            String summary = "Fakes your " + permissionType + " with " + title.toLowerCase() + ".";

            mutatorPreference.setKey(mutator);
            mutatorPreference.setTitle(title);
            mutatorPreference.setSummary(summary);
            mutatorPreference.setOnPreferenceClickListener(preference -> {
                saveMutatorPreference(permission, mutator, title);
                return false;
            });
            screen.addPreference(mutatorPreference);
        }
        setPreferenceScreen(screen);
    }

    private void saveMutatorPreference(String permission, String mutator, String title) {
        listener.onMutatorChoiceInteraction(permission, mutator);
        listener.onMutationChoiceInteraction(permission, Privacy.Mutation.FAKE);

        String info = "Saved " + Privacy.Mutation.FAKE.name() + " setting with " + title;
        Toast.makeText(getContext(), info, Toast.LENGTH_SHORT).show();
    }
}
