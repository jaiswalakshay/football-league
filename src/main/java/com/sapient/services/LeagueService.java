package com.sapient.services;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.sapient.models.LeagueDetail;
import com.sapient.utilities.HttpGetUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 
 */
@Service
public class LeagueService {

    private final static Logger log = LoggerFactory.getLogger(LeagueService.class);

    @Value("${API_KEY}")
    private String apiKey;

    @Value("standing.uri")
    private String standingUri;

    @Autowired
    private HttpGetUtility httpGetUtility;

    /**
     * @param countryName
     * @param leagueName
     * @param teamName
     * @return
     */
    public LeagueDetail getLeagueDetail(String countryName, String leagueName, String teamName) {
        LeagueDetail leagueDetail = new LeagueDetail();
        String countryId = getCountryIdByName(countryName);
        String leagueId = getLeagueIdByName(leagueName, countryId);
        ArrayNode leagueDetailsRaw = getStandings(leagueId);
        for (JsonNode node : leagueDetailsRaw) {
            if (countryName.equals(node.get("country_name").asText()) && leagueId.equals(node.get("league_id").asText()) && teamName.equals(node.get("team_name").asText())) {
                leagueDetail.setCountryId(countryId);
                leagueDetail.setCountryName(countryName);
                leagueDetail.setLeagueId(leagueId);
                leagueDetail.setLeagueName(leagueName);
                leagueDetail.setOverallLeaguePosition(node.get("overall_league_position").asText());
            }
        }
        return leagueDetail;
    }

    /**
     * @param leagueName
     * @param countryId
     * @return
     */
    public String getLeagueIdByName(String leagueName, String countryId) {
        String response = null;
        String uri = "https://apifootball.com/api/?action=get_leagues&country_id=" + countryId + "&APIkey=" + apiKey;
        ArrayNode legaueResponse = httpGetUtility.getResult(uri);
        for (JsonNode node : legaueResponse) {
            if (leagueName.equals(node.get("league_name").asText())) {
                return node.get("league_id").asText();
            }
        }

        return response;
    }

    /**
     * @param countryName
     * @return
     */
    public String getCountryIdByName(String countryName) {
        String uri = "https://apifootball.com/api/?action=get_countries&APIkey=" + apiKey;
        ArrayNode countries = httpGetUtility.getResult(uri);
        for (JsonNode node : countries) {
            if (countryName.equals(node.get("country_name").asText())) {
                return node.get("country_id").asText();
            }
        }
        return null;
    }

    /**
     * @param leagueId
     * @return
     */
    public ArrayNode getStandings(String leagueId) {
        String uri = standingUri + leagueId + "&APIkey=" + apiKey;
        ArrayNode leagueDetails = httpGetUtility.getResult(uri);
        return leagueDetails;
    }

}
