package com.example.myhello.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myhello.Classes.Loading;
import com.example.myhello.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgottenPasswordActivity extends AppCompatActivity {
    EditText etEmail;
    Button btnReset;
    TextView tvRemember;
    Loading loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);

        loading=new Loading(ForgottenPasswordActivity.this);

        etEmail = findViewById(R.id.et_email_activity_forgotten_password);
        btnReset = findViewById(R.id.btn_reset_activity_forgotten_password);
        tvRemember = findViewById(R.id.tv_remember_activity_forgotten_password);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResetPasswordEmail();
            }
        });
        tvRemember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void sendResetPasswordEmail() {
        String email = etEmail.getText().toString();
        if (!email.equals("")) {
            loading.startLoading();
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                public void onComplete(Task<Void> task) {
                    if (task.isSuccessful()) {
                        loading.dismissLoading();
                        Toast.makeText(ForgottenPasswordActivity.this, "Password reset email is sent check your mail", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ForgottenPasswordActivity.this, SignInActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        loading.dismissLoading();
                        Toast.makeText(ForgottenPasswordActivity.this, "Email is incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(ForgottenPasswordActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
        }
    }
}
