package it.smartcommunitylab.gamification.tnsmartweek.web;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class Trip {

    @DecimalMin("0.1")
    private double distance;

    @Min(1)
    private int participants;

    @Size(min = 1, message = "A team is required")
    private String selectedTeam;


    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public String getSelectedTeam() {
        return selectedTeam;
    }

    public void setSelectedTeam(String selectedTeam) {
        this.selectedTeam = selectedTeam;
    }

}
