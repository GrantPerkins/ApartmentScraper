package org.perkins;

import org.perkins.scraper.Floorplan;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HTMLBuilder {
    private final StringBuilder requestBodyStringBuilder;
    private final S3Client s3client;
    private final String bucket = "sbapartment";

    public HTMLBuilder() {
        requestBodyStringBuilder = new StringBuilder();
        writeHeader();
        s3client = S3Client.builder().region(Region.US_EAST_1).build();
    }

    private void writeHeader() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        String header = "<!DOCTYPE html><html><head><title>S&B Apartments</title></head><body><h1>Smith and Burns Availabilities</h1><p>";
        header += "Last updated: " + formattedDate + "</br></br>";
        requestBodyStringBuilder.append(header);
    }

    public void writeOldFloorPlan(Floorplan floorplan) {
        writeFloorPlan(floorplan.toString());
    }

    public void writeNewFloorPlan(Floorplan floorplan) {
        writeFloorPlan(floorplan.toNewString());
    }

    private void writeFloorPlan(String floorplan) {
        requestBodyStringBuilder.append(floorplan);
    }

    public void putToS3() {
        requestBodyStringBuilder.append("</p></body></html>");
        String filename = "index.html";
        PutObjectRequest request = PutObjectRequest
                .builder()
                .bucket(bucket)
                .key(filename)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .contentType("html")
                .build();
        s3client.putObject(request, RequestBody.fromString(requestBodyStringBuilder.toString()));
    }
}
