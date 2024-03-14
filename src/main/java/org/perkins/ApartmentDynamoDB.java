package org.perkins;

import org.perkins.scraper.Floorplan;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ApartmentDynamoDB {
    private final DynamoDbClient dynamoDbClient = DynamoDbClient.builder().region(Region.US_EAST_1).build();

    private final String tableName = "apartments";

    public ApartmentDynamoDB() {

    }

    public void putFloorplan(Floorplan floorplan) {
        HashMap<String, AttributeValue> item = new HashMap<>();
        item.put("key", AttributeValue.builder().s(floorplan.toKey()).build());
        item.put("name", AttributeValue.builder().s(floorplan.getName()).build());
        item.put("price", AttributeValue.builder().s(floorplan.getPrice()).build());
        item.put("date", AttributeValue.builder().s(floorplan.getDate()).build());
        item.put("number", AttributeValue.builder().s(floorplan.getNumber()).build());
        item.put("floor", AttributeValue.builder().s(floorplan.getFloor()).build());
        item.put("url", AttributeValue.builder().s(floorplan.getUrl()).build());
        item.put("deposit", AttributeValue.builder().s(floorplan.getDeposit()).build());
        PutItemRequest putItemRequest = PutItemRequest.builder().tableName(tableName).item(item).build();

        try {
            PutItemResponse response = dynamoDbClient.putItem(putItemRequest);
            System.out.println("Success.");
        } catch (ResourceNotFoundException e) {
            System.err.format("Error: The table \"%s\" can't be found.\n", tableName);
            System.err.println("Be sure that it exists and that you've typed its name correctly!");
            System.exit(1);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public boolean isFloorplanNew(Floorplan floorplan) {
        HashMap<String, AttributeValue> item = new HashMap<>();
        item.put("key", AttributeValue.builder().s(floorplan.toKey()).build());
        GetItemRequest request = GetItemRequest.builder().key(item).tableName(tableName).build();
        Map<String, AttributeValue> returnedItem = dynamoDbClient.getItem(request).item();
        boolean isNew = false;
        try {
            if (!returnedItem.isEmpty()) {
                Set<String> keys = returnedItem.keySet();
                System.out.println("Amazon DynamoDB table attributes: \n");

                for (String key1 : keys) {
                    System.out.format("%s: %s\n", key1, returnedItem.get(key1).toString());
                }
                isNew = false;
            } else {
                System.out.format("No item found with the key %s!\n", floorplan.toKey());
                isNew = true;
            }
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return isNew;
    }
}
