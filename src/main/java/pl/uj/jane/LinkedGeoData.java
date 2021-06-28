package pl.uj.jane;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.jena.query.*;
import org.json.JSONArray;
import org.json.JSONObject;
import pl.uj.jane.dto.Artist;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * An attempt to create a class that would connect to LinkedGeoData service, take data from it and process it.
 * It doesn't work since the service is out of order.
 */

public class LinkedGeoData {

    /**
     * A function that creates all the necessary connections.
     * @param queryString The string with querying information in SPARQL format.
     */

    public void queryLinkedGeoData (String queryString) {

        ObjectMapper objectMapper = new ObjectMapper();
        List<Artist> artistList = new ArrayList<>();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Query query = QueryFactory.create(queryString);
        QueryExecution queryExecution = QueryExecutionFactory.sparqlService("http://linkedgeodata.org/sparql", query);

        try {
            ResultSet results = queryExecution.execSelect();
            ResultSetFormatter.outputAsJSON(byteArrayOutputStream, results);
            JSONObject geoData = new JSONObject(byteArrayOutputStream.toString());
            System.out.println(geoData);
//            JSONArray artistArray = geoData.getJSONObject("results").getJSONArray("bindings");
//            for (Object artistObject : artistArray) {
//                artistList.add(objectMapper.readValue(artistObject.toString(), Artist.class));
//            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            queryExecution.close();
        }
    }

}
