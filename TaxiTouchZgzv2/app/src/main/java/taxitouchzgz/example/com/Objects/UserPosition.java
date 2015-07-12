package taxitouchzgz.example.com.Objects;

/**
 * Created by sergiolazaromagdalena on 12/7/15.
 */
public class UserPosition {

    private String username;
    private String address;
    private String lon;
    private String lat;
    private String time;

    public UserPosition(String username, String address, String lon, String lat, String time){
        this.username = username;
        this.address = address;
        this.lon = lon;
        this.lat = lat;
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setTime(String time) {this.time = time;}

    public String getTime() {return this.time;}

}
