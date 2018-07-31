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
            System.err.println(e.getMessage());
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

            System.out.println("Printing item after retrieving it....");
            System.out.println(item.toJSONPretty());

        }
        catch (Exception e) {
            fail("GetItem failed.");
            System.err.println(e.getMessage());
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

            System.out.println("Printing item that was deleted...");
            System.out.println(outcome.getItem().toJSONPretty());

        }
        catch (Exception e) {
            fail("Error deleting item in " + dynamodbTableName);
            System.err.println(e.getMessage());
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
