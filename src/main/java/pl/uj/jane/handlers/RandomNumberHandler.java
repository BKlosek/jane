package pl.uj.jane.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import pl.uj.jane.CityConnectionsSearcher;
import pl.uj.jane.LinkedGeoData;
import pl.uj.jane.WeatherReader;
import pl.uj.jane.WikiData;
import pl.uj.jane.dto.Artist;
import pl.uj.jane.dto.Destination;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class RandomNumberHandler implements IntentRequestHandler {

    private String queryString = "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n" +
            "PREFIX wd: <http://www.wikidata.org/entity/>\n" +
            "PREFIX bd: <http://www.bigdata.com/rdf#>\n" +
            "PREFIX wikibase: <http://wikiba.se/ontology#>\n" +
            "SELECT DISTINCT ?artistLabel ?artist WHERE { ?artist wdt:P106 wd:Q1028181. SERVICE wikibase:label { bd:serviceParam wikibase:language \"en\" } } limit 10";

    @Override
    public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {
        return true;
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {
        Destination dest = new CityConnectionsSearcher().RetrieveItem("AAL");
        System.out.println(dest);
        String weather = new WeatherReader().checkWeather(dest.getLat(), dest.getLong());
        List<Artist> artists = new WikiData().queryWikiData(queryString);
        String userInput = "";
        String testQueryForLinkedGeoDataCuisine = "Prefix lgdo:<http://linkedgeodata.org/ontology/> Prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#> Prefix ogc: <http://www.opengis.net/ont/geosparql#> Prefix geom: <http://geovocab.org/geometry#> SELECT ?city ?latt ?long (COUNT(?name) AS ?count) WHERE { VALUES (?city ?latt ?long) { (\"" + dest.getMunicipality() + "\" " + dest.getLat() + " " + dest.getLong() + ") } . ?instance a ?gastronomy_local ; lgdo:cuisine \"" + userInput + "\" ; rdfs:label ?name ; geom:geometry [ ogc:asWKT ?entityGeo ] . Filter ( bif:st_intersects (?entityGeo, bif:st_point (" + dest.getLong() + ", " + dest.getLat() + "),20)) . FILTER (?gastronomy_local = lgdo:Restaurant || ?gastronomy_local = lgdo:Cafe || ?gastronomy_local=lgdo:Bar) . }";
//        List<Artist> geoList = new LinkedGeoData().queryLinkedGeoData(testQueryForLinkedGeoDataCuisine);
//        System.out.println(geoList);
        if (dest != null && weather != null) {
            return handlerInput.getResponseBuilder().withSpeech("BAJO JAJO " + dest.getName() + " " + weather + "WIKIDATA NUMBER OF FOUND ARTISTS" + artists.size()).build();
        } else if (weather == null) {
            return handlerInput.getResponseBuilder().withSpeech("The weather response was empty").build();
        } else if (dest == null) {
            return handlerInput.getResponseBuilder().withSpeech("The location response was empty").build();
        }
        return handlerInput.getResponseBuilder().withSpeech("For some reason the end of the handler was reached.").build();
    }
}
