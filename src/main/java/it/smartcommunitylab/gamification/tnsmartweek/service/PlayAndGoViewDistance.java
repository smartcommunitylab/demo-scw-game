package it.smartcommunitylab.gamification.tnsmartweek.service;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "playerState")
class PlayAndGoViewDistance {

    public static class PointConceptFields {
        @Field("obj.name")
        public String name;
        @Field("obj.score")
        public double score;
    }


    public static class Concepts {

        @Field("PointConcept.Walk_Km")
        public PointConceptFields walkKm;

        @Field("PointConcept.Bike_Km")
        public PointConceptFields bikeKm;

        @Field("PointConcept.Bus_Km")
        public PointConceptFields busKm;

        @Field("PointConcept.Train_Km")
        public PointConceptFields trainKm;

    }

    public Concepts concepts;
}
