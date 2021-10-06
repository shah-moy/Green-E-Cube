package com.example.gec;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserFragment extends Fragment {

    //Declaring Variables
    Button log;
    TextView  name, email, uname, uemail, contact, ucontact;
    FirebaseUser user;
    DatabaseReference prof_ref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup)  inflater.inflate(R.layout.fragment_user, container, false);

        //Assigning variables and log out action
        log = (Button) root.findViewById(R.id.logoutbtn);
        name = root.findViewById(R.id.name);
        email = root.findViewById(R.id.email);
        uname = root.findViewById(R.id.username);
        uemail = root.findViewById(R.id.useremail);
        contact = root.findViewById(R.id.contact);
        ucontact = root.findViewById(R.id.usercontact);

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), WelcomeActivity.class);
                UserFragment.this.startActivity(intent);
                getActivity().getSupportFragmentManager().popBackStack();
                getActivity().finish();
                onStop();
                onDestroy();
            }
        });

        // prof_ref will get the database reference
        //user will get the current logged in user

        user = FirebaseAuth.getInstance().getCurrentUser();
        prof_ref = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

        prof_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfileInfo = snapshot.getValue(User.class);
                uname.setText(userProfileInfo.getFullName());
                uemail.setText(userProfileInfo.getEmail());
                ucontact.setText(userProfileInfo.getContact());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return root;
    }
}