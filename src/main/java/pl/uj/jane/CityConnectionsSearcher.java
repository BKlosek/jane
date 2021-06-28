package pl.uj.jane;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

// Import log4j classes.
//import org.apache.logging.log4j.Logger;
//import org.apache.logging.log4j.LogManager;
import pl.uj.jane.dto.Destination;

import java.util.Objects;

/**
 * Class retrieving connections from  DynamoDB and searching for connections in the retrieved data
 */

public class CityConnectionsSearcher {

    /**
     * A library method that establishes communication with AWS. It reads data from a default file with
     * credentials usually defined in "Documents\\.aws". That file is generated via AWS CLI that needs
     * to be installed prior.
     */
    private static final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withRegion(Regions.US_EAST_1).build();
    /**
     * A library class specific for DynamoDB connection on AWS.
     */
    private static DynamoDB dynamoDB = new DynamoDB(client);

    /**
     * A library class representing a table from DynamoDB.
     */
    private static final Table table = dynamoDB.getTable("airportConnectionTable");

//    private static final Logger LOGGER = LogManager.getLogger(CityConnectionsSearcher.class);

    /**
     * Given an IATA code this method retrieves a record from a DynamoDB table by the code provided. Then it
     * converts the retrieved item into
     * @param IATA The IATA code of the airport. Theoretically retrieved from "LinkedGeoData" service.
     * @return Returns an object of class Destination containing all information about the destination airport.
     */
    public Destination RetrieveItem(String IATA) {

        GetItemSpec spec = new GetItemSpec().withPrimaryKey("ID", 5);

        try{
            Item outcome = table.getItem(spec);
            if (Objects.nonNull(outcome)){
                Destination dest = new Destination();
                dest.setIATA(IATA);
                dest.setMunicipality(outcome.get("Municipality").toString());
                dest.setLat(Double.parseDouble(outcome.get("Lat").toString()));
                dest.setLong(Double.parseDouble(outcome.get("Long").toString()));
                dest.setName(outcome.get("Name").toString());
                return dest;
            }
        }catch (Exception e){
//            LOGGER.error("An exception has occurred while retrieving an item:  ", e);
            return null;
        }

        return null;

    }

}
