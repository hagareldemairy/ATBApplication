package com.example.hagarhossam.aroundtheblock_version2.UserProfile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.hagarhossam.aroundtheblock_version2.DatabaseManager.Database;
import com.example.hagarhossam.aroundtheblock_version2.NavigationMainActivity;
import com.example.hagarhossam.aroundtheblock_version2.R;

public class SignUp extends AppCompatActivity {

    EditText _emailText;
    EditText _passwordText;
    EditText _nameText;
    Button _signupButton;
    Database db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        _emailText = (EditText) findViewById(R.id.textEmail);
        _passwordText = (EditText) findViewById(R.id.textPassword);
        _signupButton = (Button) findViewById(R.id.buttonSignUp);
        _nameText = (EditText) findViewById(R.id.textName);
        db = new Database();

    }

    public void SignUpButtonClicked(View view)
    {
        String email=_emailText.getText().toString();
        String passwrod=_passwordText.getText().toString();
        String name = _nameText.getText().toString();



        if (!validate()) {
            onSignupFailed();
            return;
        }

        else {
            Boolean result;
            db = new Database();
            String resp = db.SignUp(email, passwrod, name);
            System.out.println("el responsse hya "+resp);

            if (resp.contains("Duplicate entry")){
                Toast.makeText(SignUp.this, "Sign up failed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,SignUp.class);
                startActivity(intent);

            }

            else {
                Intent intent = new Intent(this,NavigationMainActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Sign Up success", Toast.LENGTH_SHORT).show();
                intent.putExtra("email",email);
                intent.putExtra("password",passwrod);
                intent.putExtra("name", name);

            }

        }

    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String name = _nameText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            _passwordText.setError("password at least 4 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Sign-up failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }
}
