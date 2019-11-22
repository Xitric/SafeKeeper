package dk.sdu.privacyenforcer.client;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dk.sdu.privacyenforcer.client.filters.Filter;
import dk.sdu.privacyenforcer.client.filters.FineLocationFilter;
import dk.sdu.privacyenforcer.ui.Privacy;
import okhttp3.OkHttpClient;

public class PrivacyEnforcingClient implements FilterProvider {

    private SharedPreferences preferences;
    private Map<String, Filter> filters;

    public PrivacyEnforcingClient(Context context) {
        preferences = context.getSharedPreferences(Privacy.PERMISSION_PREFERENCE_FILE, Context.MODE_PRIVATE);
        filters = new HashMap<>();

        registerFilter(Privacy.Permission.SEND_LOCATION, new FineLocationFilter());
    }

    public OkHttpClient.Builder getClientBuilder() {
        FilterEngine engine = new FilterEngine(this);
        PrivacyRequestInterceptor interceptor = new PrivacyRequestInterceptor(engine);

        //Create a client with our own interceptor in the pipeline
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.interceptors().add(interceptor);
        return clientBuilder;
    }

    public void registerFilter(String permission, Filter filter) {
        filters.put(permission, filter);
    }

    @Override
    public List<Filter> getFilters() {
        List<Filter> result = new ArrayList<>();
        Set<String> permissions = preferences.getStringSet(Privacy.PERMISSION_PREFERENCES, new HashSet<>());
        if (permissions == null) return result;

        for (String permission : permissions) {
            Filter filter = filters.get(permission);

            if (filter == null) {
                Log.w("PrivacyEnforcingClient", "Missing filter for permission: " + permission);
            } else {
                String permissionMode = preferences.getString(permission, null);
                if (permissionMode != null) {
                    filter.setMode(Privacy.Mutation.valueOf(permissionMode));
                }
                result.add(filter);
            }
        }

        return result;
    }
}
