package taxitouchzgz.example.com.Objects;

/**
 * Created by sergiolazaromagdalena on 11/7/15.
 */
public class TaxiStop {

    private int id;
    private String address;
    private float lon;
    private float lat;

    public TaxiStop(int id, String address, float lon, float lat){
        this.id = id;
        this.address = address;
        this.lon = lon;
        this.lat = lat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
