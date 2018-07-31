package com.epam.hometask;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.logs.AWSLogs;
import com.amazonaws.services.logs.AWSLogsAsyncClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.epam.hometask.utils.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.BeforeAll;

import java.util.Properties;

public class CommonBase {

    public static final Log log = LogFactory.getLog(CommonBase.class);
    public static Regions region;
    public static String bucketName;
    public static String lambdaName;
    public static String dynamodbTableName;
    public static int timeout;

    public static AmazonS3 s3Client;
    public static TransferManager transferManager;
    public static AWSLogs awsLogs;
    public static AmazonDynamoDB dynamoDBClient;
    public static DynamoDB dynamodb;

    {
        s3Client = AmazonS3ClientBuilder.standard().withRegion(region).build();
        transferManager = TransferManagerBuilder.standard().withS3Client(s3Client).build();
        awsLogs = AWSLogsAsyncClientBuilder.standard().withRegion(region).build();
        dynamoDBClient = AmazonDynamoDBClientBuilder.standard().withRegion(region).build();
        dynamodb = new DynamoDB(dynamoDBClient);
    }

    @BeforeAll
    public static void init() {
        Properties properties = Utils.readProperties("test.properties");
        region = Regions.fromName(properties.getProperty("region"));
        bucketName = properties.getProperty("bucket.name");
        lambdaName = properties.getProperty("lambda.name");
        dynamodbTableName = properties.getProperty("dynamodb.name");
        timeout = Integer.valueOf(properties.getProperty("timeout"));
    }
}
