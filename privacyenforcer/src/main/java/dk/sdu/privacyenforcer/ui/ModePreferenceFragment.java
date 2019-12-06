package dk.sdu.privacyenforcer.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;

import dk.sdu.privacyenforcer.R;
import dk.sdu.privacyenforcer.client.Privacy;
import dk.sdu.privacyenforcer.client.repository.LibraryDatabase;
import dk.sdu.privacyenforcer.client.repository.MutatorDAO;

public class ModePreferenceFragment extends DynamicPreferenceFragment {

    private static final String PERMISSION_CHOICE = "permission_choice";
    private String permissionChoice;
    private PreferenceCategory mutatorCategory;
    private ListPreference mutatorChoice;

    public static ModePreferenceFragment newInstance(String permission) {
        Bundle bundle = new Bundle();
        bundle.putString(PERMISSION_CHOICE, permission);

        ModePreferenceFragment instance = new ModePreferenceFragment();
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        super.onCreatePreferences(savedInstanceState, rootKey);
        if (getArguments() != null) initFromBundle(getArguments());
        initModePreferences();
        initMutatorPreferences();
    }

    private void initFromBundle(Bundle bundle) {
        permissionChoice = bundle.getString(PERMISSION_CHOICE);
    }

    private void initModePreferences() {
        Context context = getPreferenceManager().getContext();

        PreferenceCategory modeCategory = new PreferenceCategory(context);
        modeCategory.setTitle(R.string.category_mode);
        getPreferenceScreen().addPreference(modeCategory);

        ListPreference modeChoice = new ListPreference(context);
        modeChoice.setKey(permissionChoice + Privacy.MODE_SUFFIX);
        modeChoice.setTitle(R.string.choose_permission);
        modeChoice.setSummaryProvider(new ModeSummaryProvider());
        modeChoice.setEntries(R.array.permission_mode_entries);
        modeChoice.setEntryValues(R.array.permission_mode_values);
        modeChoice.setDefaultValue(Privacy.Mutation.BLOCK.toString());
        modeChoice.setOnPreferenceChangeListener(this::modeChanged);
        modeCategory.addPreference(modeChoice);
    }

    private void initMutatorPreferences() {
        MutatorDAO mutatorDao = LibraryDatabase.getInstance(getContext()).mutatorDAO();
        mutatorDao.loadAllMutatorsIdsByType(permissionChoice).observe(this, mutatorEntities -> {
            String[] mutatorEntries = new String[mutatorEntities.size()];
            String[] mutatorValues = new String[mutatorEntities.size()];

            for (int i = 0; i < mutatorEntities.size(); i++) {
                String mutatorID = mutatorEntities.get(i).mid;

                mutatorEntries[i] = PermissionStringHelper.getAsTitle(mutatorID, getContext());
                mutatorValues[i] = mutatorID;
            }

            handleMutatorEntities(mutatorEntries, mutatorValues);
        });
    }

    private void handleMutatorEntities(String[] mutatorEntries, String[] mutatorValues) {
        Context context = getPreferenceManager().getContext();

        mutatorCategory = new PreferenceCategory(context);
        mutatorCategory.setTitle(R.string.category_mutator);
        getPreferenceScreen().addPreference(mutatorCategory);

        mutatorChoice = new ListPreference(context);
        mutatorChoice.setKey(permissionChoice + Privacy.MUTATOR_SUFFIX);
        mutatorChoice.setTitle(R.string.choose_mutator);
        mutatorChoice.setSummaryProvider(new MutatorSummaryProvider());
        mutatorChoice.setEntries(mutatorEntries);
        mutatorChoice.setEntryValues(mutatorValues);
        mutatorCategory.addPreference(mutatorChoice);

        toggleMutatorList(getPreferenceUtil().getPermissionMode(permissionChoice));
    }

    private boolean modeChanged(Preference preference, Object newValue) {
        toggleMutatorList(Privacy.Mutation.valueOf((String) newValue));
        return true;
    }

    private void toggleMutatorList(Privacy.Mutation permissionMode) {
        boolean isFakeSelected = permissionMode == Privacy.Mutation.FAKE;
        boolean areMutatorsAvailable = mutatorChoice.getEntryValues().length > 0;
        mutatorCategory.setVisible(isFakeSelected && areMutatorsAvailable);
    }

    private final class ModeSummaryProvider implements Preference.SummaryProvider<ListPreference> {

        @Override
        public CharSequence provideSummary(ListPreference preference) {
            return PermissionStringHelper.getAsTitle(preference.getValue(), preference.getContext());
        }
    }

    private final class MutatorSummaryProvider implements Preference.SummaryProvider<ListPreference> {

        @Override
        public CharSequence provideSummary(ListPreference preference) {
            return PermissionStringHelper.getAsTitle(preference.getValue(), preference.getContext());
        }
    }
}
