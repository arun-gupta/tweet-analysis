package org.sample.twitter;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

/**
 * @author arungupta
 */
public class DynamoDBUtil {

    public static void saveTwitterStatus(String json) {
        // Workaround until https://github.com/aws/aws-sdk-java/issues/1189 is fixed
        json = json.replaceAll(":\"\"", ":null");

        System.out.println("\n\njson: " + json);
        getTable().putItem(Item.fromJSON(json));
    }

    public static long lastTweetId() {
        long id = 0;

//        N1qlQuery query = N1qlQuery.simple("select id from " +  DynamoDBUtil.getBucketName() + " ORDER BY id DESC LIMIT 1");
//        List<N1qlQueryRow> result = getBucket().query(query).allRows();
//        if (result == null || result.isEmpty())
//            return id;
//
//        String idString = result.get(0).value().getString("id");
//        id = Long.parseLong(idString);
//
//        System.out.println("lastTweetId: " + id);

        return id;
    }

    private static AmazonDynamoDB dynamodbClient;
    private static Table table;

    public static final AmazonDynamoDB getClient() {
        if (null != dynamodbClient) {
            return dynamodbClient;
        }

        String region = System.getenv("DYNAMODB_REGION");
        if (null == region) {
            System.err.println("Region is null, using default \"" + Regions.US_WEST_1 + "\"");
            region = Regions.US_WEST_1.name();
        }
        System.out.println("DynamoDB region: " + region);

        dynamodbClient = AmazonDynamoDBClientBuilder.standard()
                .withRegion(region)
                .build();
        
        System.out.println("Got DynamoDB client...");

        return dynamodbClient;
    }

    public static final Table getTable() {
        if (null != table) {
            return table;
        }

        table = new DynamoDB(getClient()).getTable(getTableName());
        System.out.println("Got DynamoDB table...");
        return table;
    }

    public static String getTableName() {
        return "Twitter";
    }
}
