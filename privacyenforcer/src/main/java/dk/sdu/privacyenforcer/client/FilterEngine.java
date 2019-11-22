package dk.sdu.privacyenforcer.client;

import org.json.JSONObject;

import dk.sdu.privacyenforcer.client.filters.Filter;

class FilterEngine {

    private final FilterProvider filterProvider;

    FilterEngine(FilterProvider filterProvider) {
        this.filterProvider = filterProvider;
    }

    ViolationCollection applyFilters(RequestUrl url, JSONObject body) {
        ViolationCollection violations = new ViolationCollection();

        for (Filter filter : filterProvider.getFilters()) {
            if (url != null) filter.filter(url, violations);
            if (body != null) filter.filter(body, violations);

            if (violations.isAborted()) {
                return violations;
            }
        }

        return violations;
    }
}
