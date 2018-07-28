package com.sapient.services;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.sapient.models.LeagueDetail;

public interface LeagueService {

    LeagueDetail getLeagueDetail(String countryName, String leagueName, String teamName);

    /**
     * @param leagueName
     * @param countryId
     * @return
     */
    String getLeagueIdByName(String leagueName, String countryId);

    /**
     * @param countryName
     * @return
     */
    String getCountryIdByName(String countryName);

    /**
     * @param leagueId
     * @return
     */
    ArrayNode getStandings(String leagueId);

}
