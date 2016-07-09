package com.example.rotem.beats;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rotem.beats.Model.Model;

public class SignUpActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    Button signUpBtn;
    String emailUserInput;
    String pwdUserInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();
    }

    private void init() {
        email = (EditText) findViewById(R.id.signup_email);
        password = (EditText) findViewById(R.id.signup_password);
        signUpBtn = (Button) findViewById(R.id.signup_signup_btn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                emailUserInput = String.valueOf(email.getText());
                pwdUserInput = String.valueOf(password.getText());
                Model.getInstance().signup(emailUserInput, pwdUserInput, new Model.AuthListener() {
                    @Override
                    public void onDone(String userId, Exception e) {
                        if (e == null){
                            Toast.makeText(MyApplication.getAppContext(), "Sign up succeeded" ,
                                    Toast.LENGTH_SHORT).show();
                            Log.d("LoginActivity", "login success");

                            // start home activity
                            Intent intent = new Intent(v.getContext(), MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(MyApplication.getAppContext(), "Authentication failed: " + e.getMessage() ,
                                    Toast.LENGTH_SHORT).show();
                            Log.d("LoginActivity", e.getMessage());
                        }
                    }
                });
            }
        });

    } // end init
}
