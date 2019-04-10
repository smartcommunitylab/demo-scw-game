package it.smartcommunitylab.gamification.tnsmartweek.service;

import static it.smartcommunitylab.gamification.tnsmartweek.service.DataManager.SnapshotSource.CLIMB;
import static it.smartcommunitylab.gamification.tnsmartweek.service.DataManager.SnapshotSource.PLAY_AND_GO;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Executor {


    private static final Logger logger = LogManager.getLogger(Executor.class);

    @Autowired
    private DataManager dataManager;

    @Autowired
    private GameEngineClient engineClient;



    @PostConstruct
    public void takeBaseData() {
        if (dataManager.getLastSnapshotValue(CLIMB) == 0) {
            double distance = dataManager.storeSnapshot(CLIMB, dataManager.getClimbScore());
            logger.info("Init climb data with value {}", distance);
        }

        if (dataManager.getLastSnapshotValue(PLAY_AND_GO) == 0) {
            double distance = dataManager.storeSnapshot(PLAY_AND_GO,
                    DataManager.convertToMeters(dataManager.getPlayAndGoScore()));
            logger.info("Init playAndGo data with value {}", distance);
        }

    }

    // @Scheduled(cron = "${cron.climb}") -> NOT USED FOR EDUCA
    public double climbSnapshot() {
        double lastValue = dataManager.getLastSnapshotValue(CLIMB);
        double actualValue = dataManager.getClimbScore();
        double delta = 0d;
        if (lastValue != 0) {
            delta = actualValue - lastValue;
            logger.info("Climb Delta {}", delta);
            if (delta < 0) {
                logger.warn("ATTENTION negative delta, no action to game-engine will be sent");
            } else if (delta == 0) {
                logger.info("Delta is 0, no action to game-engine will be sent");
            } else {
                engineClient.gameClimbAction(delta);
                logger.info("Sent action to gamificationEngine");
            }
        } else {
            logger.warn("Seems that climb data is not correctly init");
        }
        if (delta > 0) {
            double result = dataManager.storeSnapshot(CLIMB, actualValue);
            if (result > 0) {
                logger.info("store actual value for climb {}", actualValue);
            } else {
                logger.error("Problem storing climb actual value");
            }
        } else {
            logger.info("Delta is 0, value is not change, nothing to store");
        }
        return delta;
    }


    // @Scheduled(cron = "${cron.playAndGo}") -> NOT USED FOR EDUCA
    public double playAndGoSnapshot() {
        double lastValue = dataManager.getLastSnapshotValue(PLAY_AND_GO);
        double actualValue = DataManager.convertToMeters(dataManager.getPlayAndGoScore());
        double delta = 0d;
        if (lastValue != 0) {
            delta = actualValue - lastValue;
            logger.info("PlayAndGo Delta {}", delta);
            if (delta < 0) {
                logger.warn("ATTENTION negative delta, no action to game-engine will be sent");
            } else if (delta < 0) {
                logger.info("Delta is 0, no action to game-engine will be sent");
            } else if (delta == 0) {
                logger.info("Delta is 0, no action to game-engine will be sent");
            } else {
                engineClient.gamePlayAndGoAction(delta);
                logger.info("Sent action to gamificationEngine");
            }
        } else {
            logger.warn("Seems that playAndGo data is not correctly init");
        }
        if (delta > 0) {
            double result = dataManager.storeSnapshot(PLAY_AND_GO, actualValue);
            if (result > 0) {
                logger.info("store actual value for playAndGo {}", actualValue);
            } else {
                logger.error("Problem storing playAndGo actual value");
            }
        } else {
            logger.info("Delta is 0, value is not change, nothing to store");
        }
        return delta;
    }
}
