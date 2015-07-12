package taxitouchzgz.example.com.taxitouchzgzv2;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sergiolazaromagdalena on 12/7/15.
 */
public class ChatView extends Activity{

    private static Context context;
    public static ListView chat;
    private ArrayAdapter<String> adapter;
    private EditText message;
    private Button send;
    public static ArrayList<String> array;
    private static String sender, receiver;
    public static ArrayAdapter<String> listAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        context = this;

        sender = getIntent().getExtras().getString("sender");
        receiver = getIntent().getExtras().getString("receiver");

        array = new ArrayList<String>();

        chat = (ListView) findViewById(R.id.chatMessages);
        message = (EditText) findViewById(R.id.chatText);

        getComments();

        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(message.getText().toString().length() > 0 && checkSQLInjection(message.getText().toString())){
                    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date date = new Date();
                    String d = dt.format(date);     //Date
                    String text = message.getText().toString();
                    new SendMessagesActivity(getApplicationContext(),array,chat,adapter).execute(sender,receiver,text,d);
                    getComments();
                }
            }
        });

    }


    /**
     * Method used to check every line inserted by users to avoid SQL Injection.
     * This method must be available on every file.
     * @param line
     * @return true if [line] doesnt contains words like DROP, INSERT INTO, DELETE FROM OR UPDATE.
     */
    private boolean checkSQLInjection(String line){
        if(!line.contains("DROP") || !line.contains("INSERT INTO") || !line.contains("DELETE FROM")
                || !line.contains("UPDATE")){
            return true;
        }
        else{
            return false;
        }
    }

    public static void getComments(){
        new GetMessagesActivity(context).execute(sender,receiver);
    }

    private void clearTextField(){
        message.setText("");
    }

    public static void setArray(ArrayList<String> a) {
        array = a;
    }
}
