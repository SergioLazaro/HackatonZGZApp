package taxitouchzgz.example.com.taxitouchzgzv2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private EditText usernameField, passwordField;
    private Button sendButton, clearButton, botonPrueba;
    private TextView signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameField = (EditText) findViewById(R.id.editText1);
        passwordField = (EditText) findViewById(R.id.editText2);

        signup = (TextView) findViewById(R.id.signup);

        sendButton = (Button) findViewById(R.id.sendButton);
        clearButton = (Button) findViewById(R.id.clearbutton);

        clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clearFields();
            }
        });

    }

    public void loginPost(View view) {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        if(checkSQLInjection(username)){
            if(checkSQLInjection(password)){

                new SigninActivity(this).execute(username,password);
            }
            else{
                Toast.makeText(this,"Change your password",Toast.LENGTH_SHORT);
            }
        }
        else{
            Toast.makeText(this,"Change your username",Toast.LENGTH_SHORT);
        }
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

    /**
     * This method clears [usernameField] and [passwordField]
     */
    private void clearFields(){
        usernameField.setText("");
        passwordField.setText("");
    }

    /**
     *
     */
    public void signup(View view){
        Intent signup = new Intent(this,SignUp.class);
        startActivity(signup);
    }
}
