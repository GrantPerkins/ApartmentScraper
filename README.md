# Smith & Burns Apartment Availability Webscraper

Seattle apartment hunting is tricky. If a unit is available Jan 1 for $2200, and you want to start your lease on Jan 3, it will cost $2300 (for example). The later you wait, the more the unit costs. Thus, I need to be on top of the available listings to avoid paying extra.

## What is this?

[An easy way to track available units in the building I want to rent from.](https://sbapartment.s3.amazonaws.com/index.html)

Running entirely on AWS.

## Design

### Lambda
Once a day at 1am, a Lambda function is run. It scrapes [https://smithandburnsseattle.com/floorplans/](https://smithandburnsseattle.com/floorplans/) for all available floorplans. Then, it scrapes each floorplan for available units.

### DynamoDB
A NoSQL database is used to keep track of new postings. If a unit is not in the DB, then it must be new!

### S3
The lambda function generates a static HTML page once a day. This page is hosted here: [https://sbapartment.s3.amazonaws.com/index.html](https://sbapartment.s3.amazonaws.com/index.html)
