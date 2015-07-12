package taxitouchzgz.example.com.taxitouchzgzv2;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MenuItem;

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
public class deleteNotification extends AsyncTask<String,Void,String> {

    private Context context;
    private String username, username2;

    //flag 0 means get and 1 means post.(By default it is get.)
    public deleteNotification(Context context) {
        this.context = context;
    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... arg0) {

        //means by Post Method
        try{
            username = (String)arg0[0];

            String link="http://uhkk977fa063.hackzgzapp.koding.io/hackaton/deleteNotification.php";
            String data  = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");

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
    }
}

