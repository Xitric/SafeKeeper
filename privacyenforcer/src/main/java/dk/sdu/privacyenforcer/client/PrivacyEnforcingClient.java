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
import dk.sdu.privacyenforcer.client.repository.LibraryDatabase;
import dk.sdu.privacyenforcer.client.repository.MutatorEntity;
import dk.sdu.privacyenforcer.location.BatteryConservingLocationReceiver;
import dk.sdu.privacyenforcer.ui.Privacy;
import okhttp3.OkHttpClient;

public class PrivacyEnforcingClient implements FilterProvider {

    private SharedPreferences preferences;
    private Map<String, Filter> filters;

    public PrivacyEnforcingClient(Context context) {
        preferences = context.getSharedPreferences(Privacy.PERMISSION_PREFERENCE_FILE, Context.MODE_PRIVATE);
        filters = new HashMap<>();

        registerFilter(Privacy.Permission.SEND_LOCATION, new FineLocationFilter(new BatteryConservingLocationReceiver(context)));
        registerMutators(context);
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


    public void registerMutators(Context context) {
        for (Map.Entry<String, Filter> filterEntry : filters.entrySet()) {
            List<String> mutatorIdentifiers = filterEntry.getValue().getMutatorIdentifiers();
            MutatorEntity[] mutatorEntities = new MutatorEntity[mutatorIdentifiers.size()];

            for (int i = 0; i < mutatorIdentifiers.size(); i++) {
                MutatorEntity mutatorEntity = new MutatorEntity();
                mutatorEntity.setMid(mutatorIdentifiers.get(i));
                mutatorEntity.setType(filterEntry.getKey());
                mutatorEntities[i] = mutatorEntity;
            }

            new Thread() {
                @Override
                public void run() {
                    LibraryDatabase.getInstance(context).mutatorDAO().insertAll(mutatorEntities);
                }
            }.start();

        }
    }

    @Override
    public List<Filter> getFilters() {
        List<Filter> result = new ArrayList<>();
        Set<String> permissions = preferences.getStringSet(Privacy.PERMISSION_PREFERENCES, new HashSet<>());

        for (Map.Entry<String, Filter> filterEntry : filters.entrySet()) {
            String permission = filterEntry.getKey();
            Filter filter = filterEntry.getValue();

            String permissionMode = preferences.getString(permission, null);
            if (permissionMode != null) {
                filter.setMode(Privacy.Mutation.valueOf(permissionMode));
            } else {
                filter.setMode(Privacy.Mutation.BLOCK);
            }
            result.add(filter);

            if (permissions != null) permissions.remove(permission);
        }

        if (permissions != null) {
            for (String permission : permissions) {
                Log.w("PrivacyEnforcingClient", "Missing filter for permission: " + permission);
            }
        }

        return result;
    }
}
