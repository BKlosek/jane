package pl.uj.jane;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.json.JSONArray;
import org.json.JSONObject;
import pl.uj.jane.utils.TypeOfQuery;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class makes queries to WikiData service.
 */

public class WikiData {

    /**
     * This is the main and the only method of this class. it queries WikiData service and returns a list
     * of artists associated with this genre of art.
     *
     * @param queryString String containing query for WikiData service
     * @return A list or objects of type Artist
     */

    public <T> List<T> queryWikiData(String queryString, Class<T> clazz, TypeOfQuery typeOfQuery) {
        List<T> resultsList = new ArrayList<>();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Query query = QueryFactory.create(queryString);
        QueryExecution queryExecution = QueryExecutionFactory.sparqlService("https://query.wikidata.org/sparql", query);
        try {
            ResultSet results = queryExecution.execSelect();
            ResultSetFormatter.outputAsJSON(byteArrayOutputStream, results);
            JSONObject resultsJSON = new JSONObject(byteArrayOutputStream.toString());
            resultsList = formatResultsDependsOnTypeOfQuery(clazz, typeOfQuery, resultsJSON);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            queryExecution.close();
        }

        return resultsList;
    }

    private <T> List<T> formatResultsDependsOnTypeOfQuery(Class<T> clazz, TypeOfQuery typeOfQuery, JSONObject resultsJSON) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<T> resultsList = new ArrayList<>();
        if (typeOfQuery.equals(TypeOfQuery.ARTISTS_LIST) || typeOfQuery.equals(TypeOfQuery.ART_MOVEMENTS_LIST)
                || typeOfQuery.equals(TypeOfQuery.ARTIST_INTENT) || typeOfQuery.equals(TypeOfQuery.ART_MOVEMENT_INTENT)) {
            JSONArray resultsArray = resultsJSON.getJSONObject("results").getJSONArray("bindings");
            for (Object resultObject : resultsArray) {
                resultsList.add(objectMapper.readValue(resultObject.toString(), clazz));
            }
        } else if (typeOfQuery.equals(TypeOfQuery.UNKNOWN_ART_INTENT)) {
            return Collections.singletonList(objectMapper.convertValue(resultsJSON.getJSONObject("results").getJSONArray("bindings").getJSONObject(0).getJSONObject("count").get("value"), clazz));
        } else {
            return Collections.emptyList();
        }

        return resultsList;
    }
}
