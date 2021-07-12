package pl.uj.jane.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import pl.uj.jane.CityConnectionsSearcher;
import pl.uj.jane.WeatherReader;
import pl.uj.jane.WikiData;
import pl.uj.jane.dto.Artist;
import pl.uj.jane.dto.Airport;
import pl.uj.jane.utils.TypeOfQuery;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static pl.uj.jane.utils.Queries.ARTISTS_LIST;

public class RandomNumberHandler implements IntentRequestHandler {

    @Override
    public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {
        return true;
    }

    @SneakyThrows
    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {
//        Destination dest = new CityConnectionsSearcher().RetrieveItem("AAL");
//        System.out.println(dest);
//        String weather = new WeatherReader().checkWeather(dest.getLat(), dest.getLong());
//        List<Artist> artists = new WikiData().queryWikiData(queryString);
//        String userInput = "";
//        String testQueryForLinkedGeoDataCuisine = "Prefix lgdo:<http://linkedgeodata.org/ontology/> Prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#> Prefix ogc: <http://www.opengis.net/ont/geosparql#> Prefix geom: <http://geovocab.org/geometry#> SELECT ?city ?latt ?long (COUNT(?name) AS ?count) WHERE { VALUES (?city ?latt ?long) { (\"" + dest.getMunicipality() + "\" " + dest.getLat() + " " + dest.getLong() + ") } . ?instance a ?gastronomy_local ; lgdo:cuisine \"" + userInput + "\" ; rdfs:label ?name ; geom:geometry [ ogc:asWKT ?entityGeo ] . Filter ( bif:st_intersects (?entityGeo, bif:st_point (" + dest.getLong() + ", " + dest.getLat() + "),20)) . FILTER (?gastronomy_local = lgdo:Restaurant || ?gastronomy_local = lgdo:Cafe || ?gastronomy_local=lgdo:Bar) . }";
//        List<Artist> geoList = new LinkedGeoData().queryLinkedGeoData(testQueryForLinkedGeoDataCuisine);
//        System.out.println(geoList);

        CityConnectionsSearcher cityConnectionsSearcher = new CityConnectionsSearcher();
//        String airport = cityConnectionsSearcher.RetrieveItem("RYG");
        Airport airport = cityConnectionsSearcher.RetrieveItem("RYG");
        ItemCollection<ScanOutcome> cities = cityConnectionsSearcher.RetrieveItems("Municipality", "Kraków");
        ObjectMapper objectMapper = new ObjectMapper();
//        Airport test = objectMapper.readValue(airport, Airport.class);
        String json = "";

        Iterator<Item> iterator = cities.iterator();
        while (iterator.hasNext()) {
            json = iterator.next().toJSON();
        }


        List<String>  destinations = Collections.emptyList();

        try {
            Airport testAirport = objectMapper.readValue(json, Airport.class);
            destinations = Arrays.asList(testAirport.getDestinationsIATA().replace("[", "").replace("]", "").split(","));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String weather = new WeatherReader().checkWeather(airport.getLat(), airport.getLon());
        List<Artist> artists = new WikiData().queryWikiData(ARTISTS_LIST, Artist.class, TypeOfQuery.ARTISTS_LIST);
//        if (airport != null && weather != null) {
            return handlerInput.getResponseBuilder().withSpeech("BAJO JAJO " + airport + " \n" + "WIKIDATA NUMBER OF FOUND ARTISTS" + artists.size() + " " + json + " " + destinations.get(0)).build();
//        } else if (weather == null) {
//            return handlerInput.getResponseBuilder().withSpeech("The weather response was empty").build();
//        } else if (weather == null) {
//            return handlerInput.getResponseBuilder().withSpeech("The location response was empty").build();
//        }
//        return handlerInput.getResponseBuilder().withSpeech("For some reason the end of the handler was reached.").build();
    }
}
