package it.smartcommunitylab.gamification.tnsmartweek.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.mongodb.WriteResult;

@Component
public class DataManager {



    @Autowired
    @Qualifier("sourcesMongoTemplate")
    private MongoTemplate sourceMongoTemplate;

    @Autowired
    @Qualifier("appMongoTemplate")
    private MongoTemplate appMongoTemplate;



    public double getClimbScore() {
        Criteria crit = Criteria.where("gameId")
                .in(Arrays.asList("58a55bece4b0d3fae8c96ff7", "5a97ea6f839cd81f52aae3a5"));
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
        Criteria crit = Criteria.where("gameId").is("59a91478e4b0c9db6800afaf");
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
}
