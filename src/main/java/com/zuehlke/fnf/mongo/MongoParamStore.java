package com.zuehlke.fnf.mongo;

import com.zuehlke.fnf.utsukushii.UtsukushiiProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 *  simple mongo storage. No need for Spring Data Mongo
 */
public class MongoParamStore implements DocumentStore<UtsukushiiProperties> {

    private static final String COLLECTION = UtsukushiiProperties.class.getSimpleName();

    @Autowired
    @Qualifier("kobayashiMongo")
    private MongoOperations mongo;

    @Override
    public void store( UtsukushiiProperties data ) {

        mongo.save(data, COLLECTION);
    }

    @Override
    public List<UtsukushiiProperties> retrieve( Query query ) {
        List<UtsukushiiProperties> data = mongo.find(query, UtsukushiiProperties.class, COLLECTION);
        return data;
    }

    @Override
    public UtsukushiiProperties retrieve( String recordId ) {
        UtsukushiiProperties config = mongo.findById(recordId, UtsukushiiProperties.class, COLLECTION);
        return config;
    }

    @Override
    public void clearAll () {
        mongo.dropCollection(COLLECTION);
    }

    @Override
    public void delete(Query query) {
        mongo.remove(query, COLLECTION);
    }
}
