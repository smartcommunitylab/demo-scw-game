package it.smartcommunitylab.gamification.tnsmartweek.service;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "playerState")
class ClimbViewTotalDistance {

    public static class PointConceptFields {
        @Field("obj.name")
        public String name;
        @Field("obj.score")
        public double score;
    }

    public static class TargetPointConcept {
        @Field("total_distance")
        public PointConceptFields pointConceptFields;
    }

    public static class Concepts {

        @Field("PointConcept")
        public TargetPointConcept pointConcept;

    }

    public Concepts concepts;

}
