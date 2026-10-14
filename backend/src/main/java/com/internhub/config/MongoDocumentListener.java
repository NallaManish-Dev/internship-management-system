package com.internhub.config;

import com.internhub.entity.*;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import java.time.Instant;

/** Assigns stable numeric IDs so existing REST routes remain backward compatible. */
@Component
public class MongoDocumentListener extends AbstractMongoEventListener<Object> {
    private final MongoOperations mongo;
    public MongoDocumentListener(MongoOperations mongo) { this.mongo = mongo; }
    @Override public void onBeforeConvert(BeforeConvertEvent<Object> event) {
        Object source = event.getSource();
        String collection = mongo.getCollectionName(source.getClass());
        if (source instanceof User user) {
            assignId(user, collection); Instant now = Instant.now(); if (user.getCreatedAt() == null) user.setCreatedAt(now); user.setUpdatedAt(now);
        } else if (source instanceof StudentProfile profile) assignId(profile, collection);
        else if (source instanceof CompanyProfile profile) assignId(profile, collection);
        else if (source instanceof Internship internship) assignId(internship, collection);
        else if (source instanceof Application application) assignId(application, collection);
        else if (source instanceof AuditLog auditLog) assignId(auditLog, collection);
    }
    private long next(String collection) {
        Document result = mongo.getCollection("counters").findOneAndUpdate(
                new Document("_id", collection), new Document("$inc", new Document("sequence", 1)),
                new com.mongodb.client.model.FindOneAndUpdateOptions().upsert(true).returnDocument(com.mongodb.client.model.ReturnDocument.AFTER));
        return result.get("sequence", Number.class).longValue();
    }
    private void assignId(User value, String collection) { if (value.getId() == null) value.setId(next(collection)); }
    private void assignId(StudentProfile value, String collection) { if (value.getId() == null) value.setId(next(collection)); }
    private void assignId(CompanyProfile value, String collection) { if (value.getId() == null) value.setId(next(collection)); }
    private void assignId(Internship value, String collection) { if (value.getId() == null) value.setId(next(collection)); }
    private void assignId(Application value, String collection) { if (value.getId() == null) value.setId(next(collection)); }
    private void assignId(AuditLog value, String collection) { if (value.getId() == null) value.setId(next(collection)); }
}
