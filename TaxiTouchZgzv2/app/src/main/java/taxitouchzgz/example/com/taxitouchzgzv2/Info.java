package taxitouchzgz.example.com.taxitouchzgzv2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by sergiolazaromagdalena on 11/7/15.
 */
public class Info extends Activity{

    private TextView license, licenseVersion, authors, alberto, hector, sergio, github,
        githubLink, contact, mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);


        license = (TextView) findViewById(R.id.signup);
        licenseVersion = (TextView) findViewById(R.id.signup);
        authors = (TextView) findViewById(R.id.signup);
        alberto = (TextView) findViewById(R.id.signup);
        hector = (TextView) findViewById(R.id.signup);
        sergio = (TextView) findViewById(R.id.signup);
        github = (TextView) findViewById(R.id.signup);
        githubLink = (TextView) findViewById(R.id.signup);
        contact = (TextView) findViewById(R.id.signup);
        mail = (TextView) findViewById(R.id.signup);

    }
}
