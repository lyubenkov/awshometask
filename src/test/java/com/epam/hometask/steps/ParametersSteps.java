package com.epam.hometask.steps;

import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import static com.epam.hometask.CommonBase.dynamodb;
import static com.epam.hometask.CommonBase.dynamodbTableName;
import static com.epam.hometask.CommonBase.log;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.fail;

public class ParametersSteps {
    public static void createItem() {
        Table table = dynamodb.getTable(dynamodbTableName);
        try {
            Item item = new Item()
                    .withPrimaryKey("packageId", "120")
                    .withNumber("originTimeStamp", 20140812)
                    .withString("fileType", "txt")
                    .withString("filePath", "hometask");
            table.putItem(item);
        }
        catch (Exception e) {
            log.info(e.getMessage());
            fail("Create items failed");
        }
    }

    public static void retrieveItem() {
        Table table = dynamodb.getTable(dynamodbTableName);

        try {
            GetItemSpec spec = new GetItemSpec()
                    .withPrimaryKey("packageId", "120",
                            "originTimeStamp", 20140812);
            Item item = table.getItem(spec);

            log.info("Printing item after retrieving it....");
            log.info(item.toJSONPretty());
        }
        catch (Exception e) {
            log.info(e.getMessage());
            fail("GetItem failed.");
        }

    }

    public static void deleteItem() {

        Table table = dynamodb.getTable(dynamodbTableName);

        try {

            DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                    .withPrimaryKey("packageId", "120"
                            ,"originTimeStamp", 20140812)
                    .withReturnValues(ReturnValue.ALL_OLD);

            DeleteItemOutcome outcome = table.deleteItem(deleteItemSpec);

            log.info("Printing item that was deleted...");
            log.info(outcome.getItem().toJSONPretty());
        }
        catch (Exception e) {
            log.info(e.getMessage());
            fail("Error deleting item in " + dynamodbTableName);
        }
    }

    public static File createTempFile() throws IOException {
        BufferedOutputStream out = null;
        try {
            File file = File.createTempFile("s3test" + UUID.randomUUID(), ".txt");

            byte[] zeroes = new byte[1024];
            out = new BufferedOutputStream(new FileOutputStream(file));
            for (int i=0; i < 100; i++) {
                out.write(zeroes);
            }
            return file;
        } finally {
            if (out != null) out.close();
        }
    }
}
