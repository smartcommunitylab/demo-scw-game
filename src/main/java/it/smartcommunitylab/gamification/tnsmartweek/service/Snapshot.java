package it.smartcommunitylab.gamification.tnsmartweek.service;

import org.springframework.data.annotation.Id;

class Snapshot {
    @Id
    public String source;
    public double score;
}
