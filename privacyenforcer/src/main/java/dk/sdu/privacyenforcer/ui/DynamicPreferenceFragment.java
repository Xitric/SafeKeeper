package dk.sdu.privacyenforcer.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import dk.sdu.privacyenforcer.PermissionPreferenceUtil;
import dk.sdu.privacyenforcer.R;
import dk.sdu.privacyenforcer.client.Privacy;

public class DynamicPreferenceFragment extends PreferenceFragmentCompat {

    private PermissionPreferenceUtil preferenceUtil;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        getPreferenceManager().setSharedPreferencesName(Privacy.PERMISSION_PREFERENCE_FILE);
        getPreferenceManager().setSharedPreferencesMode(Context.MODE_PRIVATE);
        preferenceUtil = new PermissionPreferenceUtil(getPreferenceManager().getSharedPreferences());

        addPreferencesFromResource(R.xml.preferences_empty);
    }

    PermissionPreferenceUtil getPreferenceUtil() {
        return preferenceUtil;
    }
}
