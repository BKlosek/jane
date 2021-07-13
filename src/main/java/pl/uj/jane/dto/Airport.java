package pl.uj.jane.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Airport {
    @JsonProperty("IATA")
    private String IATA;

    @JsonProperty("Municipality")
    private String municipality;

    @JsonProperty("Airport_name")
    private String airportName;

    @JsonProperty("Lat")
    private double lat;

    @JsonProperty("Lon")
    private double lon;

    @JsonProperty("Destinations_IATA")
    private String destinationsIATA;

    public Airport() {
        this.IATA = "Unknown";
        this.municipality = "Unknown";
        this.airportName = "Unknown";
        this.lat = 0.0;
        this.lon = 0.0;
        this.destinationsIATA = "Unknown";
    }

    @Override
    public String toString() {
        return "Airport{" +
                "IATA='" + IATA + '\'' +
                ", municipality='" + municipality + '\'' +
                ", airportName='" + airportName + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", destinationsIATA='" + destinationsIATA + '\'' +
                '}';
    }
}
