package pl.uj.jane.dto;

/**
 * The class representing an airport defined in DynamoDB database. It is treated as a destination.
 */
public class Destination {

    /**
     * IATA code of the airport.
     */
    private String IATA = "";
    /**
     * Municipality in which this airport is situated.
     */
    private String Municipality = "";
    private double Lat = 0;
    private double Lon = 0;
    /**
     * Name of the airport.
     */
    private String name = "";

    public String getIATA() {
        return IATA;
    }

    public void setIATA(String IATA) {
        this.IATA = IATA;
    }

    public String getMunicipality() {
        return Municipality;
    }

    public void setMunicipality(String municipality) {
        Municipality = municipality;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }


    public double getLong() {
        return Lon;
    }

    public void setLong(double lon) {
        Lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
