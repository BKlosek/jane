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
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class CityConnectionsSearcher {

    private static final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withRegion(Regions.US_EAST_2).build();
    private static DynamoDB dynamoDB = new DynamoDB(client);

    private static final Table table = dynamoDB.getTable("airportConnectionTable");

    private static final Logger LOGGER = LogManager.getLogger(CityConnectionsSearcher.class);

    public Destination RetrieveItem(String IATA) {

        GetItemSpec spec = new GetItemSpec().withPrimaryKey("ID", 1);

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
            LOGGER.error("An exception has occurred while retrieving an item:  ", e);
            return null;
        }

        return null;

    }

    public ItemCollection<QueryOutcome> RetrieveItems(String attributeName, String value){

        QuerySpec spec = new QuerySpec()
                .withKeyConditionExpression(attributeName + " = :" + attributeName)
                .withValueMap(new ValueMap()
                        .withString(":v_" + attributeName, value));

        ItemCollection<QueryOutcome> items = table.query(spec);

        for (Item item : items) {
            System.out.println(item.toJSONPretty());
        }

        return items;
    }


}
