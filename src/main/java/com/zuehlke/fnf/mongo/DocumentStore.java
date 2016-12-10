package com.zuehlke.fnf.mongo;

import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 *  dumb storage abstraction for measurement data
 *  meant to allow exchanging various options on the fly
 */
public interface DocumentStore<T> {

    void store(T object);

    List<T> retrieve(Query query);

    T retrieve(String id);

    void clearAll();

    void delete(Query query);
}
