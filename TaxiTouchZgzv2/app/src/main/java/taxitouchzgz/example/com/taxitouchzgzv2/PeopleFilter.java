package taxitouchzgz.example.com.taxitouchzgzv2;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Scanner;

import taxitouchzgz.example.com.Objects.UserPosition;

/**
 * Created by sergiolazaromagdalena on 12/7/15.
 */
public class PeopleFilter extends AsyncTask<String,Void,String> {

    private Context context;
    private String username;
    private String neighborhood;

    //flag 0 means get and 1 means post.(By default it is get.)
    public PeopleFilter(Context context) {
        this.context = context;
    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... arg0) {

        //means by Post Method
        try{
            username = (String)arg0[0];
            neighborhood = (String)arg0[1];

            String link="http://uhkk977fa063.hackzgzapp.koding.io/hackaton/peopleFilter.php";
            String data  = URLEncoder.encode("neighborhood", "UTF-8") + "=" + URLEncoder.encode(neighborhood, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                sb.append(line);
                break;
            }
            return sb.toString();


        }
        catch(Exception e){
            e.printStackTrace();
            return new String("Exception: " + e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String result){
        if(!result.equals("")){
            ArrayList<UserPosition> array = parseResult(result);
            Toast toast = Toast.makeText(context, array.size() + "people have selected " +
                    neighborhood, Toast.LENGTH_SHORT);
            toast.show();

            if (MapsActivity.points != null ) {
                for (Marker m : MapsActivity.points) {
                    m.remove();
                }
            }

            MapsActivity.points = new ArrayList<Marker>();

            for (UserPosition up:array) {
                MapsActivity.points.add(MapsActivity.mMap.addMarker(new MarkerOptions()
                        .title(up.getUsername() + " - " + up.getTime())
                        .snippet(up.getAddress())
                        .position(new LatLng(Double.parseDouble(up.getLat()), Double.parseDouble(up.getLon())))));
            }

        }
        else{
            Toast toast = Toast.makeText(context, "Nobody chose this neighborhood",
                    Toast.LENGTH_SHORT);
            toast.show();

            if (MapsActivity.points != null ) {
                for (Marker m : MapsActivity.points) {
                    m.remove();
                }
            }

        }

    }


    private ArrayList<UserPosition> parseResult(String result){
        Scanner s = new Scanner(result);
        s.useDelimiter(":");
        String username = "";
        String lon = "";
        String lat = "";
        String address = "";
        String time = "";
        ArrayList<UserPosition> array = new ArrayList<UserPosition>();
        while(s.hasNext()){
            username = s.next();
            address = s.next();
            lon = s.next();
            lat = s.next();
            time = s.next();
            time += ":" + s.next();
            array.add(new UserPosition(username,address,lon,lat, time));
        }
        s.close();
        return array;
    }
}
