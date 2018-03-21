package it.smartcommunitylab.gamification.tnsmartweek.service;

import java.io.IOException;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {"mongo.db.sources.database=smartweekScheduler-test"})
public class DataManagerTest {

    @Autowired
    private DataManager dataManager;

    @Autowired
    private MongoTemplate mongo;


    @Before
    public void cleanup() {
        mongo.getDb().dropDatabase();
    }


    private void pushTestData(String resourceName)
            throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map[] myObjects = mapper.readValue(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName),
                Map[].class);
        for (Map obj : myObjects) {
            mongo.save(obj, "playerState");
        }
    }



    @Test
    public void climbDistance() throws JsonParseException, JsonMappingException, IOException {
        pushTestData("climb-data-1.json");
        double climbDistance = dataManager.getClimbScore();
        Assert.assertEquals(58500d, climbDistance, 0d);
    }

    @Test
    public void climbDistanceWhenPlayerHasEmptyState()
            throws JsonParseException, JsonMappingException, IOException {
        pushTestData("climb-data-2.json");
        double climbDistance = dataManager.getClimbScore();
        Assert.assertEquals(58500d, climbDistance, 0d);
    }


    @Test
    public void playAndGoDistance() throws JsonParseException, JsonMappingException, IOException {
        pushTestData("playAndGo-data-1.json");
        double playAndGoDistance = dataManager.getPlayAndGoScore();
        Assert.assertEquals(935.5863416470484d, playAndGoDistance, 0d);
    }

}
