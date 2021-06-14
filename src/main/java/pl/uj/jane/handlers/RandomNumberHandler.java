package pl.uj.jane.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import pl.uj.jane.CityConnectionsSearcher;
import pl.uj.jane.WeatherReader;
import pl.uj.jane.WikiData;
import pl.uj.jane.dto.Artist;
import pl.uj.jane.dto.Destination;

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
        Destination dest = new CityConnectionsSearcher().RetrieveItem("AAE");
        String weather = new WeatherReader().checkWeather(dest.getLat(), dest.getLong());
        List<Artist> artists = new WikiData().queryWikiData(queryString);
        if (dest != null && weather != null) {
            return handlerInput.getResponseBuilder().withSpeech("BAJO JAJO " + dest.getName() + " " + weather + "WIKIDATA NUMBER OF FOUND ARTISTS" + artists.size()).build();
        } else if (weather == null) {
            return handlerInput.getResponseBuilder().withSpeech("The weather response was empty").build();
        } else {
            return handlerInput.getResponseBuilder().withSpeech("The location response was empty").build();
        }
    }
}
