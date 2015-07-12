package taxitouchzgz.example.com.taxitouchzgzv2;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import taxitouchzgz.example.com.taxitouchzgzv2.Comment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class SendMessagesActivity  extends AsyncTask<String,Void,String> {
    private Context context;
    private ArrayAdapter<String> adapter;
    private ListView list;
    private ArrayList<String> array;
    private ArrayList<Comment> commentsArray;
    private String sender, receiver, text, date;

    public SendMessagesActivity(Context context, ArrayList<String> array, ListView list, ArrayAdapter<String> adapter) {
        this.context = context;
        this.array = array;
        this.adapter = adapter;
        this.list = list;
    }

    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... arg0) {

        sender = (String)arg0[0];
        receiver = (String) arg0[1];
        text = (String) arg0[2];
        date = (String) arg0[3];

        try {
            String link = "http://uhkk977fa063.hackzgzapp.koding.io/hackaton/sendMessage.php";
            String data  = URLEncoder.encode("sender", "UTF-8") + "=" + URLEncoder.encode(sender, "UTF-8");
            data += "&" + URLEncoder.encode("receiver", "UTF-8") + "=" + URLEncoder.encode(receiver, "UTF-8");
            data += "&" + URLEncoder.encode("text", "UTF-8") + "=" + URLEncoder.encode(text, "UTF-8");
            data += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8");

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
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    protected void onPostExecute(String line){
        if(line.equalsIgnoreCase("OK")){
            //array.add(sender + "has sent you " + text + ". " + date);
            //OBLIGATORIO
            //adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, array);
            //list.setAdapter(adapter);
            ChatView.getComments();
        }

    }



}
