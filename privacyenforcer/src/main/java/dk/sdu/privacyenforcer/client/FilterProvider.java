package dk.sdu.privacyenforcer.client;

import java.util.List;

import dk.sdu.privacyenforcer.client.filters.Filter;

interface FilterProvider {

    List<Filter> getFilters();
}
