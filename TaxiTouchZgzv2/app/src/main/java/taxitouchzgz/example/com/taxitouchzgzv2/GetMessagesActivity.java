package taxitouchzgz.example.com.taxitouchzgzv2;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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


public class GetMessagesActivity  extends AsyncTask<String,Void,String> {
    private Context context;
    private ArrayAdapter<String> adapter;
    private ListView list;
    private ArrayList<String> array;
    private ArrayList<Comment> commentsArray;
    private String username1, username2;

    public GetMessagesActivity(Context context) {
        this.context = context;
    }

    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... arg0) {
        String link = "http://uhkk977fa063.hackzgzapp.koding.io/hackaton/getMessages.php";
        try {
            username1 = (String)arg0[0];
            username2 = (String)arg0[1];

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
        parseLine(line);

        ChatView.setArray(array);
        ChatView.listAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, array);
        ChatView.chat.setAdapter(ChatView.listAdapter);
    }


    private void parseLine(String line){
        //array.clear();
        Log.e("MESSAGE", line);
        array = new ArrayList<String>();
        Scanner s = new Scanner(line);
        s.useDelimiter(",");
        String commentID = "";
        String sender = "";
        String receiver = "";
        String text = "";
        String date = "";
        while(s.hasNext()){
            commentID = s.next();
            sender = s.next();
            receiver = s.next();
            text = s.next();
            date = s.next();
            //commentsArray.add(new Comment(commentID,sender,receiver,text,date));
            array.add(date + " - " + sender + ": " + text);
        }

    }
}
