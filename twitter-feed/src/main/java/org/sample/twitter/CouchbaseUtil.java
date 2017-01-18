package org.sample.twitter;

import com.couchbase.client.deps.com.fasterxml.jackson.core.JsonGenerator;
import com.couchbase.client.deps.com.fasterxml.jackson.core.JsonProcessingException;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryRow;
import java.util.List;
import java.util.concurrent.TimeUnit;
import twitter4j.Status;

/**
 * @author arungupta
 */
public class CouchbaseUtil {

    private static CouchbaseCluster cluster;
    private static Bucket bucket;

    public static void saveTwitterStatus(Status status) {
        getBucket().upsert(toJson(status)).content().toString();
    }

    public static JsonDocument toJson(Status status) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);

        String json;
        try {
            json = mapper.writeValueAsString(status);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
        System.out.println("status.getId: " + status.getId());
        System.out.println("\n\ntoJson: " + json);

        JsonDocument document = JsonDocument.create(String.valueOf(status.getId()), JsonObject.fromJson(json));
        System.out.println("\n\ndocument: " + document.content().toString());

        return document;
    }

    public static long lastTweetId() {
        long id = 0;

        N1qlQuery query = N1qlQuery.simple("select id from " +  CouchbaseUtil.getBucketName() + " ORDER BY id DESC LIMIT 1");
        List<N1qlQueryRow> result = getBucket().query(query).allRows();
        if (result == null || result.isEmpty())
            return id;

        String idString = result.get(0).value().getString("id");
        id = Long.parseLong(idString);

        System.out.println("lastTweetId: " + id);

        return id;
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
            String bucketPassword = System.getProperty("COUCHBASE_BUCKET_PASSWORD");
            if (bucketPassword == null) {
                bucketPassword = System.getenv("COUCHBASE_BUCKET_PASSWORD");
            }
            if (bucketPassword == null) {
                throw new RuntimeException("Bucket password is null");
            }
            System.out.println("bucketPassword: " + bucketPassword);
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
