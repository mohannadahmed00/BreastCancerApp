package com.example.myhello.Activities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myhello.Classes.Loading;
import com.example.myhello.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    StorageReference storageReference;
    UploadTask uploadTask;

    ImageView ivUserImg;
    EditText etFirstName, etLastName, etAge, etPhone, etEmail, etPassword, etReTypePassword;
    Button btnSignUp, btnGoogle;
    TextView tvShowPassword, tvShowRePassword, tvSignIn;
    Loading loading;

    Uri userImgLocalUri;
    String userImgGlobalUri;
    public static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        loading = new Loading(SignUpActivity.this);

        ivUserImg = findViewById(R.id.iv_user_img_activity_sign_up);
        etFirstName = findViewById(R.id.et_first_name_activity_sign_up);
        etLastName = findViewById(R.id.et_last_name_activity_sign_up);
        etAge = findViewById(R.id.et_age_activity_sign_up);
        etPhone = findViewById(R.id.et_phone_activity_sign_up);
        etEmail = findViewById(R.id.et_email_activity_sign_up);
        etPassword = findViewById(R.id.et_password_activity_sign_up);
        etReTypePassword = findViewById(R.id.et_retype_password_activity_sign_up);
        tvShowPassword = findViewById(R.id.tv_show_password_activity_sign_up);
        tvShowRePassword = findViewById(R.id.tv_show_retype_password_activity_sign_up);
        btnSignUp = findViewById(R.id.btn_sign_up_activity_sign_up);
        btnGoogle = findViewById(R.id.btn_google_activity_sign_up);
        tvSignIn = findViewById(R.id.tv_sign_in_activity_sign_up);

        ivUserImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });

        tvShowPassword.setVisibility(View.GONE);
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etPassword.getText().length() > 0) {
                    tvShowPassword.setVisibility(View.VISIBLE);
                } else {
                    tvShowPassword.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPassword(etPassword, tvShowPassword);
            }
        });

        tvShowRePassword.setVisibility(View.GONE);
        etReTypePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etReTypePassword.getText().length() > 0) {
                    tvShowRePassword.setVisibility(View.VISIBLE);
                } else {
                    tvShowRePassword.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvShowRePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPassword(etReTypePassword, tvShowRePassword);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //signUp();
                if (isConnectedToInternet()){
                    createAccount();
                }else {
                    Toast.makeText(SignUpActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignUpActivity.this, "Google+", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            userImgLocalUri = data.getData();
            ivUserImg.setImageURI(userImgLocalUri);
        }
    }

    void createAccount() {
        final String fName, lName, age, email, phone, password, rePassword;
        fName = etFirstName.getText().toString().trim();
        lName = etLastName.getText().toString().trim();
        age = etAge.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        phone = etPhone.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        rePassword = etReTypePassword.getText().toString().trim();

        if ((!fName.equals("") && !lName.equals("") && !email.equals("") && !phone.equals("") && !password.equals("")) && (password.equals(rePassword) && (password.length() >= 8) && userImgLocalUri != null)) {
            loading.startLoading();
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        storageReference = FirebaseStorage.getInstance().getReference().child("User Image").child(System.currentTimeMillis() + "." + getFileExe(userImgLocalUri));
                        uploadTask = storageReference.putFile(userImgLocalUri);
                        uploadTask.continueWithTask(new Continuation() {
                            @Override
                            public Object then(@NonNull Task task) throws Exception {
                                if (!task.isComplete()) {
                                    throw task.getException();
                                }
                                return storageReference.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    userImgGlobalUri = task.getResult().toString();
                                    databaseReference = FirebaseDatabase.getInstance().getReference().child("User Info").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                    Map<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("first name", fName);
                                    hashMap.put("last name", lName);
                                    hashMap.put("age", age);
                                    hashMap.put("email", email);
                                    hashMap.put("phone", phone);
                                    hashMap.put("user image", userImgGlobalUri);
                                    hashMap.put("last diagnosis", "-1");
                                    databaseReference.setValue(hashMap);
                                    //sendVerificationEmail();
                                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    loading.dismissLoading();
                                }
                            }
                        });


                    } else {
                        loading.dismissLoading();
                        Toast.makeText(SignUpActivity.this, "Account creation failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (userImgLocalUri == null) {
            Toast.makeText(this, "Please select image", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 8 && password.length() > 0) {
            Toast.makeText(this, "Password is too short", Toast.LENGTH_SHORT).show();
        } else if (!etPassword.getText().toString().trim().equals(etReTypePassword.getText().toString().trim())) {
            Toast.makeText(this, "Password is not match", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please enter your information", Toast.LENGTH_SHORT).show();
        }
    }

   /* private void signUp() {
        final String fName, lName, age, email, phone, password, rePassword;
        fName = etFirstName.getText().toString().trim();
        lName = etLastName.getText().toString().trim();
        age = etAge.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        phone = etPhone.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        rePassword = etReTypePassword.getText().toString().trim();

        if ((!fName.equals("") && !lName.equals("") && !email.equals("") && !phone.equals("") && !password.equals("")) && (password.equals(rePassword) && (password.length() >= 8) && userImgLocalUri != null)) {
            //loading.startLoading();
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        uploadTask = storageReference.child("User Image").child(System.currentTimeMillis() + "." + getFileExe(userImgLocalUri)).putFile(userImgLocalUri);
                        uploadTask.continueWithTask(new Continuation() {
                            @Override
                            public Object then(@NonNull Task task) throws Exception {
                                if (!task.isComplete()) {
                                    throw task.getException();
                                }
                                return storageReference.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    userImgGlobalUri = task.getResult().toString();
                                    databaseReference=FirebaseDatabase.getInstance().getReference().child("User Info").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                    Map<String, Object> user = new HashMap<>();
                                    user.put("first name", fName);
                                    user.put("last name", lName);
                                    user.put("age", age);
                                    user.put("email", email);
                                    user.put("phone", phone);
                                    user.put("user image", userImgGlobalUri);
                                    user.put("Last Diagnosis", "-1");
                                    databaseReference.setValue(user);
                                    //sendVerificationEmail();
                                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            }
                        });

                    } else {
                        Toast.makeText(SignUpActivity.this, "Account creation failed", Toast.LENGTH_SHORT).show();
                    }
                    //loading.dismissLoading();
                }
            });
        }
        else if (userImgLocalUri == null) {
            Toast.makeText(this, "Please select image", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 8 && !password.equals("") || rePassword.length() < 8 && !rePassword.equals("")) {
            Toast.makeText(this, "Password is too short", Toast.LENGTH_SHORT).show();
        } else if (!etPassword.getText().toString().trim().equals(etReTypePassword.getText().toString().trim())) {
            Toast.makeText(this, "Password is not match", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please enter your information", Toast.LENGTH_SHORT).show();
        }

    }*/

    private void showPassword(EditText password, TextView show) {
        if (show.getText().equals("show")) {
            show.setText("hide");
            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            show.setText("show");
            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        password.setSelection(password.length());
    }

    private String getFileExe(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void sendVerificationEmail() {
        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Verification mail is sent", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                } else {
                    Toast.makeText(SignUpActivity.this, "Verification error", Toast.LENGTH_SHORT).show();
                    databaseReference.child("User Info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                    FirebaseAuth.getInstance().getCurrentUser().delete();
                }
            }
        });
        loading.dismissLoading();
        FirebaseAuth.getInstance().signOut();
    }

    public boolean isConnectedToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            for (int i = 0; i < info.length; i++)
                if (info[i].getState() == NetworkInfo.State.CONNECTED)
                {
                    return true;
                }

        }
        return false;
    }

}
