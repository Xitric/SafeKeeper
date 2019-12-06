package dk.sdu.privacyenforcer.ui;

import android.content.Context;

class PermissionStringHelper {

    static String getAsTitle(String key, Context context) {
        return toTitleCase(getAsName(key, context));
    }

    static String getAsName(String key, Context context) {
        return context.getString(getIdentifier(key, context));
    }

    private static int getIdentifier(String name, Context context) {
        return context.getResources().getIdentifier(name, "string", context.getPackageName());
    }

    private static String toTitleCase(String original) {
        original = original.toLowerCase();
        String firstCharacter = String.valueOf(original.charAt(0));
        return original.replaceFirst(firstCharacter, firstCharacter.toUpperCase());
    }
}
