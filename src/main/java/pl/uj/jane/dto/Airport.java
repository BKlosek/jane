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
    private final String IATA;

    @JsonProperty("Municipality")
    private final String municipality;

    @JsonProperty("Airport_name")
    private final String airportName;

    @JsonProperty("Lat")
    private final double lat;

    @JsonProperty("Lon")
    private final double lon;

    @JsonProperty("Destinations_IATA")
    private final String destinationsIATA;

    public Airport() {
        this.IATA = "Unknown";
        this.municipality = "Unknown";
        this.airportName = "Unknown";
        this.lat = 0.0;
        this.lon = 0.0;
        this.destinationsIATA = "Unknown";
    }
}
