package pl.uj.jane.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.RequestEnvelope;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class DefaultIntentHandlerTest {

    private DefaultIntentHandler defaultIntentHandler = new DefaultIntentHandler();

    @Test
    public void shouldHandleDefaultIntentRequest() throws IOException {
        Intent intent = Intent.builder().withName("helloWorldIntent").build();
        IntentRequest intentRequest = IntentRequest.builder().withIntent(intent).build();
        RequestEnvelope requestEnvelope = RequestEnvelope.builder().withRequest(intentRequest).build();
        HandlerInput handlerInput = HandlerInput.builder().withRequestEnvelope(requestEnvelope).build();

        boolean canHandle = defaultIntentHandler.canHandle(handlerInput, intentRequest);
        Assert.assertTrue(canHandle);
    }

    @Test
    public void shouldNotHandleCustomIntentRequest() throws IOException {
        Intent intent = Intent.builder().withName("cuisine").build();
        IntentRequest intentRequest = IntentRequest.builder().withIntent(intent).build();
        RequestEnvelope requestEnvelope = RequestEnvelope.builder().withRequest(intentRequest).build();
        HandlerInput handlerInput = HandlerInput.builder().withRequestEnvelope(requestEnvelope).build();

        boolean canHandle = defaultIntentHandler.canHandle(handlerInput, intentRequest);
        Assert.assertFalse(canHandle);
    }
}