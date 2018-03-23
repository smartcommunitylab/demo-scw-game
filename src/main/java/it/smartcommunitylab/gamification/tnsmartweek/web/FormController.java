package it.smartcommunitylab.gamification.tnsmartweek.web;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@ConfigurationProperties(prefix = "form")
public class FormController {

    private String googleKey;

    private List<String> teams;

    private static final Logger logger = LogManager.getLogger(FormController.class);

    @RequestMapping("/")
    public String form(Map<String, Object> model) {
        model.put("trip", new Trip());
        model.put("teams", teams);
        model.put("googleKey", googleKey);
        return "form";
    }

    @PostMapping("/submission")
    public String process(@ModelAttribute Trip trip) {
        System.out.println(
                trip.getDistance() + " " + trip.getParticipants() + " " + trip.getSelectedTeam());
        logger.info("trip for team {}: participants: {}, distance: {}", trip.getSelectedTeam(),
                trip.getParticipants(), trip.getDistance());
        return "form";
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
