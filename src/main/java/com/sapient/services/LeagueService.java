package com.sapient.services;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.sapient.models.LeagueDetail;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class LeagueService {

    private final static Logger log = LoggerFactory.getLogger(LeagueService.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Value("${API_KEY}")
    private String apiKey ;//= "9bb66184e0c8145384fd2cc0f7b914ada57b4e8fd2e4d6d586adcc27c257a978";


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

    public String getLeagueIdByName(String leagueName, String countryId) {
        String response = null;
        String uri = "https://apifootball.com/api/?action=get_leagues&country_id=" + countryId + "&APIkey=" + apiKey;
        ArrayNode legaueResponse = getResult(uri);
        for (JsonNode node : legaueResponse) {
            if (leagueName.equals(node.get("league_name").asText())) {
                return node.get("league_id").asText();
            }
        }

        return response;
    }

    public String getCountryIdByName(String countryName) {
        String response = null;
        String uri = "https://apifootball.com/api/?action=get_countries&APIkey=" + apiKey;
        ArrayNode countries = getResult(uri);
        for (JsonNode node : countries) {
            if (countryName.equals(node.get("country_name").asText())) {
                return node.get("country_id").asText();
            }
        }
        return null;
    }

    public ArrayNode getStandings(String leagueId) {
        String uri = "https://apifootball.com/api/?action=get_standings&league_id=" + leagueId + "&APIkey=" + apiKey;
        ArrayNode leagueDetails = getResult(uri);
        return leagueDetails;
    }

    private ArrayNode getResult(String uri) {
        ArrayNode response = null;
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(uri);
        try {
            // Execute the method.
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                log.error("Method failed: " + method.getStatusLine());
            }

            // Read the response body.
            byte[] responseBody = method.getResponseBody();

            response = mapper.readValue(new String(responseBody), ArrayNode.class);

        } catch (HttpException e) {
            log.error("Fatal protocol violation: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            log.error("Fatal transport error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Release the connection.
            method.releaseConnection();
        }
        return response;
    }
//
//    public static void main(String... args) {
//        LeagueService leagueService = new LeagueService();
//        System.out.println("\n*****"+leagueService.getLeagueDetail("England","Premier League","Arsenal"));
//
//    }

}
