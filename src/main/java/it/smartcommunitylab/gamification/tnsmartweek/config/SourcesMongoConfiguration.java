package it.smartcommunitylab.gamification.tnsmartweek.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@ConfigurationProperties(prefix = "mongo.db.sources")
public class SourcesMongoConfiguration extends AbstractMongoConfig {


    @Bean(name = "sourcesMongoTemplate")
    @Primary
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory());
    }


}
