package pl.uj.jane;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import java.util.Optional;

public class RandomNumberHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return true;
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        Destination dest = new CityConnectionsSearcher().RetrieveItem("AAA");
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
