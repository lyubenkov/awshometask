package com.epam.hometask.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="Hometask")
public class HometaskTable {
    private String packageId;
    private Number originTimeStamp;
    private String fileType;
    private String filePath;

    @DynamoDBHashKey(attributeName = "packageId")
    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    @DynamoDBRangeKey(attributeName = "originTimeStamp")
    public Number getOriginTimeStamp() {
        return originTimeStamp;
    }

    public void setOriginTimeStamp(Number originTimeStamp) {
        this.originTimeStamp = originTimeStamp;
    }

    @DynamoDBAttribute(attributeName = "fileType")
    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @DynamoDBAttribute(attributeName = "filePath")
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
