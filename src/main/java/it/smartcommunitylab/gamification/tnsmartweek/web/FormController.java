package it.smartcommunitylab.gamification.tnsmartweek.web;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String process(@Valid @ModelAttribute Trip trip, BindingResult errors, Model model,
            RedirectAttributes redirectAttrs) {
        if (!errors.hasErrors()) {
            logger.info("trip for team {}: participants: {}, distance: {}", trip.getSelectedTeam(),
                    trip.getParticipants(), trip.getDistance());
            engineClient.formAction(trip.getSelectedTeam(), trip.getParticipants(),
                    DataManager.convertToMeters(trip.getDistance()));
        } else {
            logger.info("Validation errors during form submission");
            model.addAttribute("teams", teams);
            model.addAttribute("googleKey", googleKey);
            return "form";
        }
        redirectAttrs.addFlashAttribute("submission", true);
        return "redirect:/";
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
