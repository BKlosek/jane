package pl.uj.jane;

public class Destination {
    private String IATA = "";
    private String Municipality = "";
    private double Lat = 0;
    private double Lon = 0;
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
