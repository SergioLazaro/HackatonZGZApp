package taxitouchzgz.example.com.taxitouchzgzv2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
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

public class SignupActivity  extends AsyncTask<String,Void,String> {

    private Context context;
    private String username;

    public SignupActivity(Context context) {
        this.context = context;
    }

    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... arg0) {

        //means by Post Method
        try {
            username = (String) arg0[0];
            String password = (String) arg0[1];

            String link = "http://uhkk977fa063.hackzgzapp.koding.io/hackaton/singup.php";
            String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

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
            return new String("Exception: " + e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String result) {
        Log.e("RESULT SIGN UP", result);
        if (result.equalsIgnoreCase("ok")) {
            Intent firstview = new Intent(context, MapsActivity.class);
            firstview.putExtra("username", username);
            context.startActivity(firstview);
        } else {
            Toast toast = Toast.makeText(context, "Problems when trying to connect.",
                    Toast.LENGTH_SHORT);
            toast.show();

        }

    }

    /**
     * This method parse the request answer.
     *
     * @param result
     * @return If result contains Query OK, true is returned.
     * Otherwise, false is returned.
     */
    private boolean parseResult(String result) {
        boolean go = false;
        Scanner s = new Scanner(result);
        String answer = s.next();
        s.close();
        if (answer.contains("Query OK")) {
            go = true;
        }
        return go;
    }
}
