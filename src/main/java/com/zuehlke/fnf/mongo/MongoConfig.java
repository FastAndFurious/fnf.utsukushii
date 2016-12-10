package com.zuehlke.fnf.mongo;

import com.mongodb.MongoClient;
import com.zuehlke.fnf.utsukushii.UtsukushiiProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {

    @Autowired
    private UtsukushiiProperties props;

    @Bean
    public MongoParamStore documentStore() {
        return new MongoParamStore();
    }

    @Bean
    public MongoOperations kobayashiMongo() {
        return new MongoTemplate(new MongoClient(props.getMongoUrl()), props.getMongoDb());
    }

}
