package pl.uj.jane;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.uj.jane.dto.Airport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(CityConnectionsSearcher.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Given an IATA code this method retrieves a record from a DynamoDB table by the code provided. Then it
     * converts the retrieved item into
     *
     * @param IATA The IATA code of the airport. Theoretically retrieved from "LinkedGeoData" service.
     * @return Returns an object of class Destination containing all information about the destination airport.
     */
    public List<Airport> findConnectionsFrom(String city) {
        String outcomeJson = "";
        List<String> destinationsIATA = new ArrayList<>();
        ItemCollection<ScanOutcome> outcome = RetrieveItems("Municipality", city);

        for (Item item : outcome) {
            outcomeJson = item.toJSON();
        }
        try {
            Airport homeAirport = objectMapper.readValue(outcomeJson, Airport.class);
            destinationsIATA = Arrays.asList(homeAirport.getDestinationsIATA()
                    .replace("[", "")
                    .replace("]", "")
                    .replace("'", "")
                    .split(","));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        List<Airport> destinationAirports = new ArrayList<>();

        //Querying one item makes no sense, but it is correlated with DynamoDB free account's read limit
        for (String IATA : destinationsIATA.stream().limit(1).collect(Collectors.toList())) {
            destinationAirports.add(RetrieveItem(IATA));
        }

        return destinationAirports;
    }

    public Airport RetrieveItem(String IATA) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("IATA", IATA)
                .withProjectionExpression("Municipality, Destinations_IATA, Lat, Lon, Airport_name, IATA");
        try {
            Item item = table.getItem(spec);
            if (Objects.nonNull(item)) {
                return objectMapper.readValue(item.toJSON(), Airport.class);
            }
        } catch (Exception e) {
            LOGGER.error("An exception has occurred while retrieving an item:  ", e);
        }
        return new Airport();
    }

    public ItemCollection<ScanOutcome> RetrieveItems(String attributeName, String value) {
        Map<String, Object> expressionAttributeValues = new HashMap<String, Object>();
        expressionAttributeValues.put(":v", value);

        return table.scan(attributeName + " = :v",
                "Municipality, Destinations_IATA, Lat, Lon, Airport_name, IATA",
                null, expressionAttributeValues);
    }
}
