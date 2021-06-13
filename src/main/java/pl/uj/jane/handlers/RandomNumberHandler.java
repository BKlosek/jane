package pl.uj.jane.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import pl.uj.jane.CityConnectionsSearcher;
import pl.uj.jane.WeatherReader;
import pl.uj.jane.dto.Destination;

import java.util.Optional;

public class RandomNumberHandler implements IntentRequestHandler {
    @Override
    public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {
        return true;
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {
        Destination dest = new CityConnectionsSearcher().RetrieveItem("AAE");
        String weather = new WeatherReader().checkWeather(dest.getLat(), dest.getLong());
        if (dest != null && weather != null){
            return handlerInput.getResponseBuilder().withSpeech("BAJO JAJO " + dest.getName() + " " + weather).build();
        }else if (weather == null){
            return handlerInput.getResponseBuilder().withSpeech("The weather response was empty").build();
        }else{
            return handlerInput.getResponseBuilder().withSpeech("The location response was empty").build();
        }
    }
}
