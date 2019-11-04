package dk.sdu.privacyenforcer.client;

import dk.sdu.privacyenforcer.client.filters.Filter;

class FilterEngine {

    private final FilterProvider filterProvider;

    FilterEngine(FilterProvider filterProvider) {
        this.filterProvider = filterProvider;
    }

    ViolationCollection applyFilters(RequestBody context) {
        ViolationCollection violations = new ViolationCollection();

        for (Filter filter : filterProvider.getFilters()) {
            filter.filter(context, violations);

            if (violations.isAborted()) {
                return violations;
            }
        }

        return violations;
    }
}
