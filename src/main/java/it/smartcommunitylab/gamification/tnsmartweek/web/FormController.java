package it.smartcommunitylab.gamification.tnsmartweek.web;

import java.util.Arrays;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@ConfigurationProperties(prefix = "form")
public class FormController {

    private String googleKey;

    @RequestMapping("/")
    public String form(Map<String, Object> model) {
        model.put("trip", new Trip());
        model.put("teams", Arrays.asList("Team SCWC", "Stand FBK"));
        model.put("googleKey", googleKey);
        return "form";
    }

    @PostMapping("/submission")
    public String process(@ModelAttribute Trip trip) {
        System.out.println(
                trip.getDistance() + " " + trip.getParticipants() + " " + trip.getSelectedTeam());
        return "form";
    }

    public String getGoogleKey() {
        return googleKey;
    }

    public void setGoogleKey(String googleKey) {
        this.googleKey = googleKey;
    }

}
