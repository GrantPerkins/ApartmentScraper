package org.perkins;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.perkins.scraper.Floorplan;
import org.perkins.scraper.FloorplanScraper;

import java.util.Map;

// Handler value: example.Handler
public class Handler implements RequestHandler<Map<String, String>, Void> {

    @Override
    public Void handleRequest(Map<String, String> event, Context context) {
        LambdaLogger logger = context.getLogger();
        FloorplanScraper fpScraper = new FloorplanScraper();
        ApartmentDynamoDB dynamoDB = new ApartmentDynamoDB();
        HTMLBuilder htmlBuilder = new HTMLBuilder();
        for (Floorplan floorplan : fpScraper.getAvailabilities()) {
            logger.log(floorplan.toKey());
            if (dynamoDB.isFloorplanNew(floorplan)) {
                // new place, send an email
                htmlBuilder.writeNewFloorPlan(floorplan);
                dynamoDB.putFloorplan(floorplan);
            } else {
                htmlBuilder.writeOldFloorPlan(floorplan);
            }
        }
        htmlBuilder.putToS3();
        return null;
    }
}