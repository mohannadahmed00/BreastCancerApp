package com.example.myhello.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText etCurrent;
    EditText etNew;
    EditText etRetypeNew;
    TextView tvShowCurrent;
    TextView tvShowNew;
    TextView tvShowRetypeNew;
    Button btnCancel;
    Button btnSave;
    Loading loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        loading = new Loading(ChangePasswordActivity.this);

        etCurrent = findViewById(R.id.et_current_activity_change_password);
        etNew = findViewById(R.id.et_new_activity_change_password);
        etRetypeNew = findViewById(R.id.et_retype_activity_change_password);
        tvShowCurrent = findViewById(R.id.tv_show_current_activity_change_password);
        tvShowNew = findViewById(R.id.tv_show_new_activity_change_password);
        tvShowRetypeNew = findViewById(R.id.tv_show_retype_activity_change_password);
        btnSave = findViewById(R.id.btn_save_activity_change_password);
        btnCancel = findViewById(R.id.btn_cancel_activity_change_password);
        tvShowCurrent.setVisibility(View.GONE);
        etCurrent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etCurrent.getText().length() > 0) {
                    tvShowCurrent.setVisibility(View.VISIBLE);
                } else {
                    tvShowCurrent.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvShowCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPassword(etCurrent, tvShowCurrent);
            }
        });

        tvShowNew.setVisibility(View.GONE);
        etNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etNew.getText().length() > 0) {
                    tvShowNew.setVisibility(View.VISIBLE);
                } else {
                    tvShowNew.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvShowNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPassword(etNew, tvShowNew);
            }
        });

        tvShowRetypeNew.setVisibility(View.GONE);
        etRetypeNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etRetypeNew.getText().length() > 0) {
                    tvShowRetypeNew.setVisibility(View.VISIBLE);
                } else {
                    tvShowRetypeNew.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvShowRetypeNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPassword(etRetypeNew, tvShowRetypeNew);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void changePassword() {
        final String current, newPassword, retype;
        current = etCurrent.getText().toString().trim();
        newPassword = etNew.getText().toString().trim();
        retype = etRetypeNew.getText().toString().trim();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (!current.equals("") && !newPassword.equals("") && !retype.equals("") && newPassword.equals(retype) && newPassword.length() >= 8) {
            loading.startLoading();
            user.reauthenticate(EmailAuthProvider.getCredential(user.getEmail(), current)).addOnCompleteListener(new OnCompleteListener<Void>() {
                public void onComplete(Task<Void> task) {
                    if (task.isSuccessful()) {
                        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(Task<Void> task) {
                                if (task.isSuccessful()) {
                                    FirebaseAuth.getInstance().signOut();
                                    Intent intent = new Intent(ChangePasswordActivity.this, SignInActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);

                                } else {
                                    Toast.makeText(ChangePasswordActivity.this, "password not changed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "incorrect password", Toast.LENGTH_SHORT).show();
                    }
                    loading.dismissLoading();
                }
            });
        } else if (current.equals("") || newPassword.equals("") || retype.equals("")) {
            Toast.makeText(this, "Please enter your information", Toast.LENGTH_SHORT).show();
        } else if (!newPassword.equals(retype)) {
            Toast.makeText(this, "password not match", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "password too short", Toast.LENGTH_SHORT).show();
        }
    }


    private void showPassword(EditText password, TextView show) {
        if (show.getText().equals("show")) {
            show.setText("hide");
            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            password.setSelection(password.length());
        } else {
            show.setText("show");
            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            password.setSelection(password.length());
        }
    }

}
