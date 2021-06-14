package pl.uj.jane;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.json.JSONArray;
import org.json.JSONObject;
import pl.uj.jane.dto.Artist;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class WikiData {

    public List<Artist> queryWikiData(String queryString) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Artist> artistList = new ArrayList<>();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Query query = QueryFactory.create(queryString);
        QueryExecution queryExecution = QueryExecutionFactory.sparqlService("https://query.wikidata.org/sparql", query);
        try {
            ResultSet results = queryExecution.execSelect();
            ResultSetFormatter.outputAsJSON(byteArrayOutputStream, results);
            JSONObject artists = new JSONObject(byteArrayOutputStream.toString());
            JSONArray artistArray = artists.getJSONObject("results").getJSONArray("bindings");
            for (Object artistObject : artistArray) {
                artistList.add(objectMapper.readValue(artistObject.toString(), Artist.class));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            queryExecution.close();
        }

        return artistList;
    }
}
