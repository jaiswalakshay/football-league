package com.sapient.services;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.sapient.models.LeagueDetail;
import com.sapient.utilities.Constants;
import com.sapient.utilities.HttpGetUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.sapient.utilities.Constants.*;

/**
 *
 */
@Service
public class APIFootBallLeagueService implements LeagueService {

    private final static Logger log = LoggerFactory.getLogger(APIFootBallLeagueService.class);

    @Value("${API_KEY}")
    private String apiKey;

    @Value("${standing.uri}")
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
            if (countryName.equals(node.get(COUNTRY_NAME).asText()) && leagueId.equals(node.get(LEAGUE_ID).asText()) && teamName.equals(node.get(TEAM_NAME).asText())) {
                leagueDetail.setCountryId(countryId);
                leagueDetail.setCountryName(countryName);
                leagueDetail.setLeagueId(leagueId);
                leagueDetail.setLeagueName(leagueName);
                leagueDetail.setOverallLeaguePosition(node.get(OVERALL_LEAGUE_POSITION).asText());
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
                return node.get(Constants.LEAGUE_ID).asText();
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
            if (countryName.equals(node.get(COUNTRY_NAME).asText())) {
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
