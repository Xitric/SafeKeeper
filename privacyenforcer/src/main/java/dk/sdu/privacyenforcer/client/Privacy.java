package dk.sdu.privacyenforcer.client;

/**
 * Interface containing constants for configuring privacy settings.
 */
public interface Privacy {

    /**
     * The name of the file containing permission preferences.
     */
    String PERMISSION_PREFERENCE_FILE = "PrivacyEnforcerPermissions";

    /**
     * The key for the preference storing a set of all configured permissions.
     */
    String PERMISSION_PREFERENCES = "privacyenforcer_permissions";

    /**
     * The suffix for specifying that a permission key is used as a mode
     */
    String MODE_SUFFIX = "_mode";

    /**
     * The suffix for specifying that a permission key is used as a mutator
     */
    String MUTATOR_SUFFIX = "_mutator";

    /**
     * Enum for representing the action to take in regards to various types of data. Data can either
     * be allowed in all outgoing requests, it can be blocked entirely, or it can be substituted
     * with fake counterparts.
     */
    enum Mutation {
        ALLOW, BLOCK, FAKE
    }

    /**
     * Interface containing constant strings for representing various types of data that a user
     * might wish to control. Strings are used, as it allows for clients to easily define new
     * strings.
     */
    interface Permission {
        String SEND_LOCATION = "privacyenforcer_permission_SEND_LOCATION";
        String SEND_CONTACTS = "privacyenforcer_permission_SEND_CONTACTS";
        String SEND_ACCELERATION = "privacyenforcer_permission_SEND_ACCELERATION";
    }

    interface LocationMutators {
        String LOCAL_OBFUSCATION = "privacyenforcer_location_mutator_LOCAL_OBFUSCATION";
        String K_ANONYMITY = "privacyenforcer_location_mutator_K_ANONYMITY";
    }

}
