package dk.sdu.privacyenforcer.ui;

import android.content.Context;

public class PermissionHelper {

    public static String getPermissionTitle(String permission, Context context) {
        return getPermissionText(permission, context).toUpperCase();
    }

    public static String getPermissionText(String permission, Context context) {
        int id = context.getResources().getIdentifier(permission, "string", context.getPackageName());
        return context.getString(id);
    }

}
