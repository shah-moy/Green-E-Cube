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
import com.google.firebase.database.FirebaseDatabase;

public class RegActivity extends AppCompatActivity {

//Initializing Vairables

    private Button regBtn;
    private EditText nameText, emailText, passText, verpassText, contactText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_reg);

        //Assigning variables to their ID
        mAuth = FirebaseAuth.getInstance();
        regBtn = findViewById(R.id.signbtn);
        nameText = findViewById(R.id.inp_name);
        emailText = findViewById(R.id.inp_email);
        passText = findViewById(R.id.inp_pass);
        verpassText = findViewById(R.id.inp_verpass);
        contactText = findViewById(R.id.inp_con);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }


//taking in input of credentials from user

    private void registerUser() {
        String name = nameText.getText().toString().trim();
        String email = emailText.getText().toString().trim();
        String pass = passText.getText().toString().trim();
        String verpass = verpassText.getText().toString().trim();
        String contact = contactText.getText().toString().trim();

        //validating and setting few constraints
        if (name.isEmpty()) {
            nameText.setError("Name Required!");
            nameText.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            emailText.setError("Email Required!");
            emailText.requestFocus();
            return;
        }
        //To see if email is valid email like example@gmail.com
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
        if (!pass.contentEquals(verpass)) {
            verpassText.setError("Password does not match");
            verpassText.requestFocus();
            return;
        }

        if (contact.isEmpty()) {
            contactText.setError("Contact No. Required");
            contactText.requestFocus();
            return;
        }
        if (!Patterns.PHONE.matcher(contact).matches()) {
            contactText.setError("Please provide valid number!");
            contactText.requestFocus();
            return;
        }

        //for authenticating and registering user
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //If informations are valid , user is created successfully
                        if (task.isSuccessful()) {
                            User user = new User(name, email, contact);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()) //we take users info and set it correspondingly with a User Id
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override

                                //User Registered Successfully
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegActivity.this, "Account Created", Toast.LENGTH_LONG).show();
                                        //  startActivity(new Intent(RegActivity.this, MainActivity.class));

                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        user.sendEmailVerification();
                                        Toast.makeText(RegActivity.this, "Confirm your registration through verification link sent in email and sign in", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(RegActivity.this, LoginActivity.class));
                                    } else {
                                        Toast.makeText(RegActivity.this, "Register Fail! Try again!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegActivity.this, "Account already exists!", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }
}
