package pl.uj.jane.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.requestType;

/**
 * Mock class to handle Cuisine intent from Jane.
 */

public class CuisineIntentHandler implements IntentRequestHandler {

    /**
     * Jane intent handlers need to have a method checking if the input can be handled by thin intent.
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
     * @param handlerInput
     * @param intentRequest
     * @return Boolean value of whether this request can be handled.
     */
    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {
        return handlerInput.getResponseBuilder()
                .withSpeech("Hi, I am in "
                        + intentRequest.getIntent().getName()
                        + " intent and I am looking for "
                        + intentRequest.getIntent().getSlots().toString())
                .build();
    }
}
