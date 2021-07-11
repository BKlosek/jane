package pl.uj.jane.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import lombok.AllArgsConstructor;
import pl.uj.jane.CityConnectionsSearcher;
import pl.uj.jane.WikiData;
import pl.uj.jane.dto.Airport;
import pl.uj.jane.dto.LocationWithQuantity;
import pl.uj.jane.utils.TypeOfQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.requestType;
import static pl.uj.jane.utils.Queries.UNKNOWN_ART_INTENT;
import static pl.uj.jane.utils.Queries.fillQueryWithParameters;

/**
 * Mock class to handle Art intent from Jane.
 */

@AllArgsConstructor
public class ArtIntentHandler implements IntentRequestHandler {

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
                intentRequest.getIntent().getName().equals("cuisine");
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
                .withSpeech("Hi, I am in "
                        + intentRequest.getIntent().getName()
                        + " intent and I am looking for "
                        + intentRequest.getIntent().getSlots().toString())
                .build();
    }

    private String handleArtIntent(IntentRequest intentRequest) {
        Slot painterSlot = intentRequest.getIntent().getSlots().get("painter");
        Slot artMovementSlot = intentRequest.getIntent().getSlots().get("artMovement");
        Optional<String> artist = Optional.ofNullable(painterSlot.getValue());
        Optional<String> artMovement = Optional.ofNullable(artMovementSlot.getValue());

//        if (artist.isPresent()) {
//            return handlePainter(artist.get());
//        } else if (artMovement.isPresent()) {
//            return handleArtMovement();
//        }
//        else {
        handleUnknownArtIntent();
//        }

        return "";
    }

    private String handleUnknownArtIntent() {
        List<Airport> destinationsFromCity = cityConnectionsSearcher.findConnectionsFrom("Kraków");
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

        //TRZEBA SPRAWDZIC POGODE, ogolnie dokonczyc ten przyapdek
        return "";
    }

//    private String handleArtMovement() {
//        List<String> destinationsFromCity = cityConnectionsSearcher.findConnectionsFrom("Kraków");
//    }
//
//    private String handlePainter(String artistName) {
//        List<Artist> artists = wikiData.queryWikiData(ARTISTS_LIST, Artist.class, TypeOfQuery.ARTISTS_LIST);
//        Optional<Artist> wantedArtist = artists.stream().filter(artist -> artist.name.equals(artistName)).findFirst();
//        List<String> destinationsFromCity = cityConnectionsSearcher.findConnectionsFrom("Kraków");
//        return "";
//    }
}
