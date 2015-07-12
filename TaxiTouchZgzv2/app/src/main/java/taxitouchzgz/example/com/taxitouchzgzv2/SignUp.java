package taxitouchzgz.example.com.taxitouchzgzv2;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by sergiolazaromagdalena on 11/7/15.
 */
public class SignUp extends Activity {

    private EditText usernameField,passwordField,repasswordField;
    private TextView usernameLabel,passwordLabel,repasswordLabel;
    private Button clearButton, confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        usernameLabel = (TextView) findViewById(R.id.newUsername);
        passwordLabel = (TextView) findViewById(R.id.newPassword);
        repasswordLabel = (TextView) findViewById(R.id.newRePassword);


        usernameField = (EditText) findViewById(R.id.newUsernameField);
        passwordField = (EditText) findViewById(R.id.newPasswordField);
        repasswordField = (EditText) findViewById(R.id.newRePasswordField);

        clearButton = (Button) findViewById(R.id.clearSignUp);
        confirmButton = (Button) findViewById(R.id.confirmSignUp);

    }

    public void login(View view) {
        //Getting text fields
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        String repassword = repasswordField.getText().toString();

        //Avoid SQL Injection
        if(checkSQLInjection(username)){
            if(checkSQLInjection(password) && checkSQLInjection(repassword)){
                if(password.equalsIgnoreCase(repassword)){
                    //Lets go
                    new SignupActivity(this).execute(username,password);
                }
                else{
                    //Password must match
                    Toast.makeText(this, "Password must match", Toast.LENGTH_SHORT);
                }
            }
            else{
                //SQL Injection on passwords fields
                Toast.makeText(this, "Change your password", Toast.LENGTH_SHORT);
            }
        }
        else{
            //SQL Injection on username field
            Toast.makeText(this,"Change your username",Toast.LENGTH_SHORT);
        }
    }

    /**
     * This method clears [usernameField], [passwordField] and [repasswordField]
     */
    public void clearFields(View view){
        usernameField.setText("");
        passwordField.setText("");
        repasswordField.setText("");
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
}
