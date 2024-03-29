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

import dk.sdu.privacyenforcer.PermissionPreferenceUtil;
import dk.sdu.privacyenforcer.client.filters.Filter;
import dk.sdu.privacyenforcer.client.filters.FineLocationFilter;
import dk.sdu.privacyenforcer.client.repository.LibraryDatabase;
import dk.sdu.privacyenforcer.client.repository.MutatorDAO;
import dk.sdu.privacyenforcer.client.repository.MutatorEntity;
import dk.sdu.privacyenforcer.location.BatteryConservingLocationReceiver;
import okhttp3.OkHttpClient;

public class PrivacyEnforcingClient implements FilterProvider {

    private PermissionPreferenceUtil preferenceUtil;
    private Map<String, Filter> filters;
    private MutatorDAO mutatorDao;

    public PrivacyEnforcingClient(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Privacy.PERMISSION_PREFERENCE_FILE, Context.MODE_PRIVATE);
        preferenceUtil = new PermissionPreferenceUtil(preferences);
        mutatorDao = LibraryDatabase.getInstance(context).mutatorDAO();
        filters = new HashMap<>();

        registerFilter(Privacy.Permission.SEND_LOCATION, new FineLocationFilter(new BatteryConservingLocationReceiver(context)));
        registerMutators();
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


    public void registerMutators() {
        for (Map.Entry<String, Filter> filterEntry : filters.entrySet()) {
            Set<String> mutatorIdentifiers = filterEntry.getValue().getMutators().keySet();
            List<MutatorEntity> mutatorEntities = new ArrayList<>();

            for (String mutator : mutatorIdentifiers) {
                MutatorEntity mutatorEntity = new MutatorEntity();
                mutatorEntity.setMid(mutator);
                mutatorEntity.setType(filterEntry.getKey());
                mutatorEntities.add(mutatorEntity);
            }

            new Thread(() -> mutatorDao.insertAll(mutatorEntities)).start();
        }
    }

    @Override
    public List<Filter> getFilters() {
        List<Filter> result = new ArrayList<>();
        Set<String> permissions = new HashSet<>(preferenceUtil.getPermissions());

        for (Map.Entry<String, Filter> filterEntry : filters.entrySet()) {
            String permission = filterEntry.getKey();
            Filter filter = filterEntry.getValue();

            Privacy.Mutation permissionMode = preferenceUtil.getPermissionMode(permission);
            filter.setMode(permissionMode);

            if (permissionMode == Privacy.Mutation.FAKE) {
                filter.setDataMutator(preferenceUtil.getMutator(permission));
            }

            result.add(filter);
            permissions.remove(permission);
        }

        for (String permission : permissions) {
            Log.w("PrivacyEnforcingClient", "Missing filter for permission: " + permission);
        }

        return result;
    }
}
