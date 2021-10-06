package com.example.gec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailText, passText;
    private Button logBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        //initiallizing variables

        logBtn = findViewById(R.id.logbtn);
        emailText = findViewById(R.id.inp_logemail);
        passText = findViewById(R.id.inp_logpass);

        //setting on clicker property

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

        mAuth = FirebaseAuth.getInstance();
    }

    //User login function
    private void userLogin() {
        String email = emailText.getText().toString().trim();
        String pass = passText.getText().toString().trim();

        //performing few validations:
        if(email.isEmpty()){
            emailText.setError("Email is required!");
            emailText.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Please enter valid email");
            emailText.requestFocus();
            return;
        }
        if (pass.isEmpty()) {
            passText.setError("Password Required!");
            passText.requestFocus();
            return;
        }
        if (pass.length() < 6) {
            passText.setError("Password should me minimum 6 characters");
            passText.requestFocus();
            return;
        }

        //To sign user in
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    //To check if User verified his email
                    if(user.isEmailVerified()){
                        //redirects to home activity
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();


                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(LoginActivity.this,"Confirm your through verification link sent in email",Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Failed to Login. Please check credentials",Toast.LENGTH_LONG).show();;
                }
            }
        });
    }
}
