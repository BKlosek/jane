package pl.uj.jane.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.Getter;

import java.io.IOException;

@JsonDeserialize(using = LocationWithQuantity.LocationWithQuantityDeserializer.class)
@Getter
public class LocationWithQuantity {

    private final String municipality;
    private final double lat;
    private final double lon;
    private final int quantity;

    @Override
    public String toString() {
        return "LocationWithQuantity{" +
                "municipality='" + municipality + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", quantity='" + quantity + '\'' +
                '}';
    }

    public LocationWithQuantity() {
        this.municipality = "Unknown";
        this.lat = Double.MIN_VALUE;
        this.lon = Double.MIN_VALUE;
        this.quantity = Integer.MIN_VALUE;
    }

    public LocationWithQuantity(double lat, double lon, int quantity) {
        this.municipality = "Unknown";
        this.lat = lat;
        this.lon = lon;
        this.quantity = quantity;
    }

    public LocationWithQuantity(String municipality, double lat, double lon, int quantity) {
        this.municipality = municipality;
        this.lat = lat;
        this.lon = lon;
        this.quantity = quantity;
    }

    public static class LocationWithQuantityDeserializer extends StdDeserializer<LocationWithQuantity> {

        public LocationWithQuantityDeserializer() {
            this(null);
        }

        public LocationWithQuantityDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public LocationWithQuantity deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonNode node = jp.getCodec().readTree(jp);

            //Conversion http://www.wikidata.org/entity/Q2599 -> Q2599
            String[] latLonStringSplitted = node.get("coord").get("value").toString().replace("(", " ").replace(")", " ").split(" ");
            double lat = Double.parseDouble(latLonStringSplitted[latLonStringSplitted.length - 3]);
            double lon = Double.parseDouble(latLonStringSplitted[latLonStringSplitted.length - 2]);
            int quantity = Integer.parseInt(node.get("count").get("value").asText());

            return new LocationWithQuantity(lat, lon, quantity);
        }
    }
}
