package com.sapient.services;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.sapient.models.LeagueDetail;

public interface LeagueService {

    public LeagueDetail getLeagueDetail(String countryName, String leagueName, String teamName);

    /**
     * @param leagueName
     * @param countryId
     * @return
     */
    public String getLeagueIdByName(String leagueName, String countryId);

    /**
     * @param countryName
     * @return
     */
    public String getCountryIdByName(String countryName);

    /**
     * @param leagueId
     * @return
     */
    public ArrayNode getStandings(String leagueId);

}
