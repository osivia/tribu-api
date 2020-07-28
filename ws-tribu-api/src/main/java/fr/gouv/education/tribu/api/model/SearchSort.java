package fr.gouv.education.tribu.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Search sorts enumeration.
 *
 * @author Loïc Billon
 */
public enum SearchSort {

    /**
     * Relevance.
     */
    @JsonProperty("relevance")
    RELEVANCE(null),

    /**
     * Last modification date.
     */
    @JsonProperty("last-modification")
    LAST_MODIFICATION("dc:modified");


    /**
     * NXQL "ORDER BY".
     */
    private final String orderBy;


    /**
     * Constructor.
     *
     * @param orderBy NXQL "ORDER BY"
     */
    SearchSort(String orderBy) {
        this.orderBy = orderBy;
    }


    public String getOrderBy() {
        return orderBy;
    }
}
