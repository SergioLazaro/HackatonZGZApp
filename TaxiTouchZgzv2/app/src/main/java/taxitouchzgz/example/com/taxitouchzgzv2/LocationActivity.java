package taxitouchzgz.example.com.taxitouchzgzv2;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class LocationActivity  extends AsyncTask<String,Void,String>{
    private Context context;
    private String username, address, lat, lon, date;
    private int hours, minutes;


    //flag 0 means get and 1 means post.(By default it is get.)
    public LocationActivity(Context context)
    {
        this.context = context;
    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... arg0) {

        //means by Post Method
        try{
            username = (String)arg0[0];
            address = (String)arg0[1];
            lon = (String) arg0[2];
            lat = (String) arg0[3];
            date = (String) arg0[4];
            //hours = Integer.parseInt(arg0[4]);
           // minutes = Integer.parseInt (arg0[5]);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String hoursUp = date.getHours() + hours + "";
            String minutesUp = date.getMinutes() + minutes + "";

            String link="http://uhkk977fa063.hackzgzapp.koding.io/hackaton/location.php";
            String data  = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(address, "UTF-8");
            data += "&" + URLEncoder.encode("lon", "UTF-8") + "=" + URLEncoder.encode(lon, "UTF-8");
            data +="&" + URLEncoder.encode("lat", "UTF-8") + "=" + URLEncoder.encode(lat, "UTF-8");
            data +="&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(this.date, "UTF-8");
            //data +="&" + URLEncoder.encode("hours", "UTF-8") + "=" + URLEncoder.encode(hoursUp, "UTF-8");
            //data +="&" + URLEncoder.encode("minutes", "UTF-8") + "=" + URLEncoder.encode(minutesUp, "UTF-8");

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
        boolean go = parseResult(result);
        if(go){
            //Intent map = new Intent(context, MapsActivity.class);
            //map.putExtra("username",username);
            //context.startActivity(map);
            //this.finish();
        }
        else{
            //Toast toast = Toast.makeText(context, "Wrong user or password",
            //        Toast.LENGTH_SHORT);
           // toast.show();

        }

    }

    /**
     * This method parse the request result.
     * @param result
     * @return If result is equals to OK, true is returned.
     * Otherwise, false is returned.
     */
    private boolean parseResult(String result){
        boolean go = false;
        Scanner s = new Scanner(result);
        String answer = s.next();
        s.close();
        if(answer.equalsIgnoreCase("INSERTUPDATE") || answer.equalsIgnoreCase("UPDATEUPDATE") ){
            go = true;
        }
        return go;
    }

}
