package taxitouchzgz.example.com.taxitouchzgzv2;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
 * Created by Alberto on 12/07/2015.
 */
public class SendRequest extends AsyncTask<String,Void,String> {

    private Context context;
    private String username1, username2;

    //flag 0 means get and 1 means post.(By default it is get.)
    public SendRequest(Context context) {
        this.context = context;
    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... arg0) {

        //means by Post Method
        try{
            username1 = (String)arg0[0];
            username2 = (String)arg0[1];


            String link="http://uhkk977fa063.hackzgzapp.koding.io/hackaton/sendRequest.php";
            String data  = URLEncoder.encode("username1", "UTF-8") + "=" + URLEncoder.encode(username1, "UTF-8");
            data  += "&" + URLEncoder.encode("username2", "UTF-8") + "=" + URLEncoder.encode(username2, "UTF-8");

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
        if(result.equals("ok")){
           Toast.makeText(context, "Request sended to " + username1, Toast.LENGTH_SHORT).show();
        }
        else{
            Toast toast = Toast.makeText(context, "Request failed",
                    Toast.LENGTH_SHORT);
            toast.show();
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
