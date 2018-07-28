package com.sapient.controllers;


import com.sapient.models.LeagueDetail;
import com.sapient.services.LeagueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Controller
@RequestMapping("/v1")
public class LeagueController {

    private static final Logger logger = LoggerFactory.getLogger(LeagueController.class);

    @Autowired
    private LeagueService leagueLeagueService;

    /**
     * @param countryName
     * @param leagueName
     * @param teamName
     * @return
     */
    @GetMapping("/league")
    public ResponseEntity<LeagueDetail> getLeagueByCountryName(@RequestParam(value = "country_name", required = true) String countryName,
                                                               @RequestParam(value = "league_name", required = true) String leagueName,
                                                               @RequestParam(value = "team_name", required = true) String teamName) {


        return new ResponseEntity<>(leagueLeagueService.getLeagueDetail(countryName, leagueName, teamName), HttpStatus.OK);
    }

    /**
     * @return
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
