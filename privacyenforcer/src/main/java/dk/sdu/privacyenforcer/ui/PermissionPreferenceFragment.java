package dk.sdu.privacyenforcer.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;

import java.util.Set;

import dk.sdu.privacyenforcer.R;

public class PermissionPreferenceFragment extends DynamicPreferenceFragment {

    public static PermissionPreferenceFragment newInstance() {
        return new PermissionPreferenceFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        super.onCreatePreferences(savedInstanceState, rootKey);
        initPermissionPreferences();
    }

    private void initPermissionPreferences() {
        Context context = getPreferenceManager().getContext();
        Set<String> permissions = getPreferenceUtil().getPermissions();
        assert permissions != null;

        PreferenceCategory permissionCategory = new PreferenceCategory(context);
        permissionCategory.setTitle(R.string.category_permissions);
        getPreferenceScreen().addPreference(permissionCategory);

        for (String permission : permissions) {
            PreferenceScreen subScreen = getPreferenceManager().createPreferenceScreen(context);
            subScreen.setKey(permission);
            subScreen.setTitle(PermissionStringHelper.getAsTitle(permission, context));
            subScreen.setFragment(ModePreferenceFragment.class.getCanonicalName());
            permissionCategory.addPreference(subScreen);
        }
    }
}
