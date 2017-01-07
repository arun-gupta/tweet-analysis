package org.sample.twitter;

import com.couchbase.client.deps.com.fasterxml.jackson.core.JsonProcessingException;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import java.util.concurrent.TimeUnit;
import twitter4j.Status;

/**
 * @author arungupta
 */
public class CouchbaseUtil {

    private static CouchbaseCluster cluster;
    private static Bucket bucket;

    public static void saveJson(Status status) {
        getBucket().upsert(toJson(status)).content().toString();
    }

    public static JsonDocument toJson(Status status) {
        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            json = mapper.writeValueAsString(status);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
        System.out.println("toJson: " + json);

        JsonDocument document = JsonDocument.create(String.valueOf(status.getId()), JsonObject.fromJson(json));

        return document;
    }

    public static CouchbaseCluster getCluster() {
        if (null == cluster) {
            String host = System.getProperty("COUCHBASE_HOST");
            if (host == null) {
                host = System.getenv("COUCHBASE_HOST");
            }
            if (host == null) {
                throw new RuntimeException("Hostname is null");
            }
            System.out.println("env: " + host);
            cluster = CouchbaseCluster.create(host);
        }
        return cluster;
    }

    public static Bucket getBucket() {
        while (null == bucket) {
            System.out.println("Trying to connect to the database");
            bucket = getCluster().openBucket(getBucketName(), 2L, TimeUnit.MINUTES);

            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                System.out.println("Thread sleep Exception: " + e.toString());
                throw new RuntimeException(e);
            }
        }

        return bucket;
    }

    public static String getBucketName() {
        return "twitter";
    }
}
