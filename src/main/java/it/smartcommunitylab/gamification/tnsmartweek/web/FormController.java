package it.smartcommunitylab.gamification.tnsmartweek.web;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.smartcommunitylab.gamification.tnsmartweek.service.DataManager;
import it.smartcommunitylab.gamification.tnsmartweek.service.GameEngineClient;

@Controller
@ConfigurationProperties(prefix = "form")
public class FormController {

    private static final Logger logger = LogManager.getLogger(FormController.class);

    private String googleKey;

    private List<String> teams;

    @Autowired
    private GameEngineClient engineClient;

    @RequestMapping("/")
    public String form(Map<String, Object> model) {
        model.put("trip", new Trip());
        model.put("teams", teams);
        model.put("googleKey", googleKey);
        return "form";
    }

    @PostMapping("/submission")
    public String process(@ModelAttribute Trip trip) {
        logger.info("trip for team {}: participants: {}, distance: {}", trip.getSelectedTeam(),
                trip.getParticipants(), trip.getDistance());
        engineClient.formAction(trip.getSelectedTeam(), trip.getParticipants(),
                DataManager.convertToMeters(trip.getDistance()));
        return "forward:/";
    }

    public String getGoogleKey() {
        return googleKey;
    }

    public void setGoogleKey(String googleKey) {
        this.googleKey = googleKey;
    }

    public List<String> getTeams() {
        return teams;
    }

    public void setTeams(List<String> teams) {
        this.teams = teams;
    }

}
