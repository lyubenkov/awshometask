package com.epam.hometask;

import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.s3.model.Bucket;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static com.epam.hometask.steps.ParametersSteps.*;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParametersTests extends CommonBase {

    @DisplayName("Check in S3 that hometask bucket is exist")
    @Test
    public void hometaskBucketShouldExist() {
        List<String> bucketNames = s3Client.listBuckets().stream().map(Bucket::getName).collect(Collectors.toList());
        assertTrue(bucketNames.contains(bucketName), format("Can't find {0} in bucket list", bucketName));
    }

    @DisplayName("Check that it's possible to create, retrieve and delete items in dynamodb table")
    @Test
    public void checkCRUDofDynamodbTable() {
        createItem();
        retrieveItem();
        deleteItem();
    }


    @DisplayName("Check table schema")
    @Test
    public void checkTableParmeters() {
        TableDescription tableDesc = dynamodb.getTable(dynamodbTableName).describe();
        assertEquals("packageId", tableDesc.getKeySchema().get(0).getAttributeName(),
                "Invalid primary key");
        assertEquals("originTimeStamp", tableDesc.getKeySchema().get(1).getAttributeName(),
                "Invalid range key");
        assertEquals("ACTIVE", tableDesc.getTableStatus());
    }
}
