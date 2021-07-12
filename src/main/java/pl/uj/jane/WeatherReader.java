package pl.uj.jane;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherReader {

    private static Map<String, Object> jsonToMap(String str) throws JsonProcessingException {
        return new ObjectMapper().readValue(str, HashMap.class);
//        List<MyClass> myObjects = mapper.readValue(jsonInput, new TypeReference<List<MyClass>>(){});
//        return new Gson().fromJson(
//            str, new TypeToken<HashMap<String, Object>>() {}.getType()
//        );
    }

    public static String checkWeather(double lat, double lon){
        String urlString = "http://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&appid=efc04d1cadba4ca9e5cac19ee685b876&units=metric";
//        String urlString = "http://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=efc04d1cadba4ca9e5cac19ee685b876&units=metric";
        try{

            StringBuilder input = new StringBuilder();
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null)
                input.append(line);
            rd.close();

            Map<String, Object> respMap = jsonToMap(input.toString());
            List<Object> weatherList = (List<Object>) respMap.get("list"); // all predictions
            Map<String, Object> weatherMap = (Map<String, Object>) weatherList.get(0);
            weatherList = (List<Object>) weatherMap.get("weather");
            weatherMap = (Map<String, Object>) weatherList.get(0);

            // if needed, this method can return a list of conditions
            return (String) weatherMap.get("main");

        }catch (IOException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    public static double checkWeatherScore(double lat, double lon){

        String weatherState = checkWeather(lat, lon);

        double score = 0;

        switch (weatherState){
            case "CLEAR":
                score += 0.02;
                break;
            case "CLOUDS":
                score += 0.01;
                break;
            case "ATMOSPHERE":
                score -= 0.002;
                break;
            case "DRIZZLE":
                score -= 0.004;
                break;
            case "SNOW":
                score -= 0.006;
                break;
            case "RAIN":
                score -= 0.0125;
                break;
            case "THUNDERSTORM":
                score -= 0.02;
                break;
        }

        return score;
    }

}
