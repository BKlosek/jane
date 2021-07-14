package pl.uj.jane.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import lombok.AllArgsConstructor;
import org.apache.jena.base.Sys;
import pl.uj.jane.CityConnectionsSearcher;
import pl.uj.jane.WikiData;
import pl.uj.jane.dto.Airport;
import pl.uj.jane.dto.ArtMovement;
import pl.uj.jane.dto.LocationWithQuantity;
import pl.uj.jane.utils.TypeOfQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.amazon.ask.request.Predicates.requestType;
import static pl.uj.jane.WeatherReader.checkWeatherScore;
import static pl.uj.jane.utils.Queries.ARTISTS_LIST;
import static pl.uj.jane.utils.Queries.ARTIST_INTENT;
import static pl.uj.jane.utils.Queries.UNKNOWN_ART_INTENT;
import static pl.uj.jane.utils.Queries.fillQueryWithParameters;

@AllArgsConstructor
public class PainterIntentHandler implements IntentRequestHandler {

    public static final String CITY = "city";
    public static final String PAINTER = "painter";
    public static final String ART_MOVEMENT = "artMovement";

    private final WikiData wikiData;
    private final CityConnectionsSearcher cityConnectionsSearcher;

    /**
     * Jane intent handlers need to have a method checking if the input can be handled by thin intent.
     *
     * @param handlerInput
     * @param intentRequest
     * @return Boolean value of whether this request can be handled.
     */
    @Override
    public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {
        return handlerInput.matches(requestType(IntentRequest.class)) &&
                intentRequest.getIntent().getName().equals("painter");
    }

    /**
     * The method that should actually handle the intent request.
     *
     * @param handlerInput
     * @param intentRequest
     * @return Boolean value of whether this request can be handled.
     */
    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {
        String response = handleArtIntent(intentRequest);
        return handlerInput.getResponseBuilder()
                .withSpeech(response)
                .build();
    }

    private String handleArtIntent(IntentRequest intentRequest) {

        // In Jane we configure slots with pieces of data that can later be retrieved here as a "Slot" class
        Map<String, Slot> slotNameToSlot = intentRequest.getIntent().getSlots();
        Set<String> slotsNames = slotNameToSlot.keySet();

        if (slotsNames.contains(CITY)) {
            String city = slotNameToSlot.get(CITY).getValue();
            if (slotsNames.contains(PAINTER)) {
                String painter = slotNameToSlot.get(PAINTER).getValue();
                return handlePainter(city, painter);
//            if (slotsNames.contains(ART_MOVEMENT)) {
//                String artMovement = slotNameToSlot.get(ART_MOVEMENT).getValue();
//                System.out.println(artMovement);
//                return handleArtMovement(city, artMovement);
            } else {
//                return handleUnknownPainter(city);
                return "Couldn't recognize this painter or if it was provided at all";
            }
        } else {
            return "Something went wrong, I can't find the city you want to leave";
        }
    }

    private String handleUnknownPainter(String city) {
        List<Airport> destinationsFromCity = cityConnectionsSearcher.findConnectionsFrom(city);
        List<LocationWithQuantity> locationsWithQuantity = new ArrayList<>();

        destinationsFromCity.forEach(destinationFromCity -> {
            List<Integer> count = wikiData.queryWikiData(fillQueryWithParameters(UNKNOWN_ART_INTENT,
                    destinationFromCity.getLat(), destinationFromCity.getLon()),
                    Integer.class, TypeOfQuery.UNKNOWN_ART_INTENT);

            LocationWithQuantity locationWithQuantity =
                    new LocationWithQuantity(destinationFromCity.getMunicipality(),
                            destinationFromCity.getLat(), destinationFromCity.getLon(),
                            count.get(0));

            locationsWithQuantity.add(locationWithQuantity);
        });

        LocationWithQuantity bestLocation = getBestLocationBasedOnWeather(locationsWithQuantity);

        return "To discover more about art, you should travel to " + bestLocation.getMunicipality() +
                " where you'll find more than " + bestLocation.getQuantity() + " of masterpieces.";
    }

    private String handlePainter(String city, String painter) {
        List<LocationWithQuantity> filteredLocationsWithQuantity = new ArrayList<>();
        List<Airport> allAvailableDestinationsFromCity = cityConnectionsSearcher.findConnectionsFrom(city);
        System.out.println(Arrays.toString(allAvailableDestinationsFromCity.toArray()));

        List<ArtMovement> painters = wikiData.queryWikiData(ARTISTS_LIST, ArtMovement.class, TypeOfQuery.ART_MOVEMENTS_LIST);
        System.out.println(Arrays.toString(painters.toArray()));

        Optional<ArtMovement> wantedPainter = painters.stream().filter(artMovement -> artMovement.name.equalsIgnoreCase(painter)).findFirst();
        System.out.println(wantedPainter);

        if (wantedPainter.isPresent()) {
            System.out.println(wantedPainter);
            List<LocationWithQuantity> allAvailableLocationsWithQuantity = wikiData.queryWikiData(fillQueryWithParameters(ARTIST_INTENT, wantedPainter.get().id), LocationWithQuantity.class, TypeOfQuery.ART_MOVEMENT_INTENT);
            System.out.println(Arrays.toString(allAvailableLocationsWithQuantity.toArray()));

            allAvailableLocationsWithQuantity.forEach(locationWithQuantity -> {
                if (filteredLocationsWithQuantity.size() < 50) {
                    allAvailableDestinationsFromCity.forEach(destination -> {
                        if (Math.sqrt(Math.pow(destination.getLat() - locationWithQuantity.getLat(), 2)
                                + Math.pow(destination.getLon() - locationWithQuantity.getLon(), 2)) * 68.1 < 20) {
                            filteredLocationsWithQuantity.add(new LocationWithQuantity(destination.getMunicipality(),
                                    destination.getLat(),
                                    destination.getLon(),
                                    locationWithQuantity.getQuantity()));
                        }
                    });
                }
            });

            System.out.println(Arrays.toString(filteredLocationsWithQuantity.toArray()));

            LocationWithQuantity bestLocation = getBestLocationBasedOnWeather(filteredLocationsWithQuantity);
            return "To discover more about " + painter + ", you should travel to " + bestLocation.getMunicipality() +
                    " where you'll find more than " + bestLocation.getQuantity() + " paintings of this movement.";
        }

        return "Something went wrong, I can't find the art movement you want to discover";
    }

//    private String handlePainter(String artistName) {
//        List<Artist> artists = wikiData.queryWikiData(ARTISTS_LIST, Artist.class, TypeOfQuery.ARTISTS_LIST);
//        Optional<Artist> wantedArtist = artists.stream().filter(artist -> artist.name.equals(artistName)).findFirst();
//        List<String> destinationsFromCity = cityConnectionsSearcher.findConnectionsFrom("Kraków");
//        return "";
//    }

    private LocationWithQuantity getBestLocationBasedOnWeather(List<LocationWithQuantity> locationsWithQuantity) {
        int bestScore = Integer.MIN_VALUE;
        LocationWithQuantity bestLocation = new LocationWithQuantity();
        for (LocationWithQuantity locationWithQuantity : locationsWithQuantity) {
            int quantity = locationWithQuantity.getQuantity();
            double lat = locationWithQuantity.getLat();
            double lon = locationWithQuantity.getLon();
            double score = checkWeatherScore(lat, lon) * (locationWithQuantity.getQuantity() + 1) + quantity;
            if (score > bestScore) {
                bestLocation = locationWithQuantity;
            }
        }
        return bestLocation;
    }
}
