package it.smartcommunitylab.gamification.tnsmartweek.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.mongodb.WriteResult;

@Component
@ConfigurationProperties(prefix = "games")
public class DataManager {

    private List<String> climb;
    private String playAndGo;

    @Autowired
    @Qualifier("sourcesMongoTemplate")
    private MongoTemplate sourceMongoTemplate;

    @Autowired
    @Qualifier("appMongoTemplate")
    private MongoTemplate appMongoTemplate;

    public double getClimbScore() {
        Criteria crit = Criteria.where("gameId").in(climb);
        Query q = new Query(crit);
        List<ClimbViewTotalDistance> states =
                sourceMongoTemplate.find(q, ClimbViewTotalDistance.class);
        return states.parallelStream().map(s -> {
            if (s.concepts != null) {
                if (s.concepts.pointConcept != null) {
                    if (s.concepts.pointConcept.pointConceptFields != null) {
                        return s.concepts.pointConcept.pointConceptFields.score;
                    }
                }
            }
            return 0d;
        }).reduce(Double::sum).orElse(0d);
    }


    public double getPlayAndGoScore() {
        Criteria crit = Criteria.where("gameId").is(playAndGo);
        Query q = new Query(crit);
        List<PlayAndGoViewDistance> states =
                sourceMongoTemplate.find(q, PlayAndGoViewDistance.class);
        return states.parallelStream().map(s -> {
            double total = 0;
            if (s.concepts != null) {
                if (s.concepts.walkKm != null) {
                    total += s.concepts.walkKm.score;
                }
                if (s.concepts.bikeKm != null) {
                    total += s.concepts.bikeKm.score;
                }
                if (s.concepts.busKm != null) {
                    total += s.concepts.busKm.score;
                }
                if (s.concepts.trainKm != null) {
                    total += s.concepts.trainKm.score;
                }
            }
            return total;
        }).reduce(Double::sum).orElse(0d);
    }

    public static double convertToMeters(double kmDistance) {
        return kmDistance * 1000;
    }

    public double getLastSnapshotValue(SnapshotSource source) {
        Criteria crit = new Criteria("source").is(source);
        Snapshot snapshot = appMongoTemplate.findOne(new Query(crit), Snapshot.class);
        if (snapshot == null) {
            return 0d;
        } else {
            return snapshot.score;
        }
    }


    public double storeSnapshot(SnapshotSource source, double score) {
        Criteria crit = Criteria.where("source").is(source);
        Update update = new Update();
        update.set("score", score);
        WriteResult result = appMongoTemplate.upsert(new Query(crit), update, Snapshot.class);
        return result.getN() == 1 ? score : 0d;
    }

    public static enum SnapshotSource {
        CLIMB, PLAY_AND_GO
    }

    public List<String> getClimb() {
        return climb;
    }


    public void setClimb(List<String> climb) {
        this.climb = climb;
    }


    public String getPlayAndGo() {
        return playAndGo;
    }


    public void setPlayAndGo(String playAndGo) {
        this.playAndGo = playAndGo;
    }
}
