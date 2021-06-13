package pl.uj.jane.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.amazon.ask.request.Predicates.requestType;

public class DefaultIntentHandler implements IntentRequestHandler {

    private List<String> customIntentNames;

    public DefaultIntentHandler() {
        this.customIntentNames = Stream.of("activity", "cuisine", "art").collect(Collectors.toList());
    }

    @Override
    public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {
        return handlerInput.matches(requestType(IntentRequest.class)) &&
                customIntentNames.stream().noneMatch(intentName -> intentName.equals(intentRequest.getIntent().getName()));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {
        return handlerInput.getResponseBuilder()
                .withSpeech("Hi, I am in " + intentRequest.getIntent().getName() + " intent")
                .build();
    }
}
