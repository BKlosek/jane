package pl.uj.jane.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.requestType;

public class ActivityIntentHandler implements IntentRequestHandler {

    @Override
    public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {
        return handlerInput.matches(requestType(IntentRequest.class)) && intentRequest.getIntent().getName().equals("activity");
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {
        return handlerInput.getResponseBuilder()
                .withSpeech("Hi, I am in " + intentRequest.getIntent().getName() + " intent")
                .build();
    }
}
