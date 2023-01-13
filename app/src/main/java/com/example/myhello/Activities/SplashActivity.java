package com.example.myhello.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myhello.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    TextView tvWelcome;
    String imgURI, name, age, phone, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        tvWelcome = findViewById(R.id.tv_welcome_activity_splash);

        try {
            databaseReference.child("User Info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    name = dataSnapshot.child("first name").getValue(String.class) + " " + dataSnapshot.child("last name").getValue(String.class);
                    tvWelcome.setText("Hello, " + name);
                    imgURI = dataSnapshot.child("user image").getValue(String.class);
                    age = dataSnapshot.child("age").getValue(String.class);
                    phone = dataSnapshot.child("phone").getValue(String.class);
                    email = dataSnapshot.child("email").getValue(String.class);
                }

                public void onCancelled(DatabaseError databaseError) {
                }
            });
        } catch (NullPointerException n) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        }
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("imgURI", imgURI);
                intent.putExtra("name", name);
                intent.putExtra("age", age);
                intent.putExtra("phone", phone);
                intent.putExtra("email", email);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, 3000);
    }
}
