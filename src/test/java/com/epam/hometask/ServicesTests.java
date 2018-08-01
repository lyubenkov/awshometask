package com.epam.hometask;

import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.epam.hometask.utils.Utils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Iterator;
import java.util.UUID;

import static com.epam.hometask.steps.ServicesSteps.isLambdaInvoked;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServicesTests extends CommonBase {

    private String uploadedFileName = "s3testFile" + UUID.randomUUID();

    @AfterEach
    public void cleanUp() {
        ObjectListing objectListing = s3Client.listObjects(bucketName);
        while (true) {
            Iterator<S3ObjectSummary> objIter = objectListing.getObjectSummaries().iterator();
            while (objIter.hasNext()) {
                String objectKey = objIter.next().getKey();
                s3Client.deleteObject(bucketName, objectKey);
                log.debug(format("Cleanup: removing {0} key.", objectKey));
            }

            if (objectListing.isTruncated()) {
                objectListing = s3Client.listNextBatchOfObjects(objectListing);
            } else {
                break;
            }
        }
    }

    @DisplayName("Lambda should be triggered after file is uploaded")
    @ParameterizedTest
    @ValueSource(strings = {"test100k.db"})
    public void lambdaShouldBeTriggeredAfterFileIsUploaded(String fileName) throws InterruptedException {
        transferManager.upload(bucketName, uploadedFileName, Utils.getFileFromResources(fileName)).waitForCompletion();
        assertTrue(isLambdaInvoked(uploadedFileName), "Can't find lambda invocation in log");
    }

    @DisplayName("Lambda should be triggered after file is uploaded and deleted")
    @ParameterizedTest
    @ValueSource(strings = {"test100k.db"})
    public void lambdaShouldBeTriggeredAfterFileIsUploadedAndDeleted(String fileName) throws InterruptedException {
        transferManager.upload(bucketName, uploadedFileName, Utils.getFileFromResources(fileName)).waitForCompletion();
        s3Client.deleteObject(bucketName, uploadedFileName);
        assertTrue(isLambdaInvoked(uploadedFileName), "Can't find lambda invocation in log");
    }
}
