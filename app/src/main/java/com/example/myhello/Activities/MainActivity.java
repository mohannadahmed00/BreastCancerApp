package com.example.myhello.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.myhello.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.example.myhello.Adapters.MyPagerAdapter;
import com.example.myhello.Classes.Loading;

public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {
    private static int RESULT_LOAD_IMAGE = 1;
    String age;
    DatabaseReference databaseReference;
    String email;
    String imgGlobalURI;
    ImageView ivConfirm;
    ImageView ivIgnore;
    ImageView ivUserImg;
    Loading loading;
    String name;
    NavigationView navigationView;
    MyPagerAdapter myPagerAdapter;
    String phone;
    StorageReference storageReference;
    TabLayout tabLayout;
    TextView tvAge;
    TextView tvEmail;
    TextView tvPhone;
    TextView tvUsername;
    UploadTask uploadTask;
    String userImgGlobalUri;
    Uri userImgLocalUri;
    ViewPager viewPager;
    View header,nothing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        databaseReference = FirebaseDatabase.getInstance().getReference();
        loading = new Loading(MainActivity.this);
        imgGlobalURI = getIntent().getStringExtra("imgURI");
        name = getIntent().getStringExtra("name");
        age = getIntent().getStringExtra("age");
        email = getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");
        navigationView = findViewById(R.id.nav_view);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        navigationView.setNavigationItemSelectedListener(this);
        header = navigationView.getHeaderView(0);
        ivUserImg = header.findViewById(R.id.nav_img);
        tvUsername = header.findViewById(R.id.nav_name);
        tvAge = header.findViewById(R.id.nav_age);
        tvEmail = header.findViewById(R.id.nav_email);
        tvPhone = header.findViewById(R.id.nav_tel);
        ivConfirm = header.findViewById(R.id.nav_confirm);
        ivIgnore = header.findViewById(R.id.nav_ignore);
        nothing=header.findViewById(R.id.nav_nothing);
        ivConfirm.setVisibility(View.GONE);
        ivIgnore.setVisibility(View.GONE);
        nothing.setVisibility(View.GONE);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            public void onTabUnselected(TabLayout.Tab tab) {
            }

            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(myPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        if (name == null || imgGlobalURI == null || age == null || email == null || phone == null) {
            databaseReference.child("User Info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    imgGlobalURI = dataSnapshot.child("user image").getValue(String.class);
                    Glide.with(getApplicationContext()).load(imgGlobalURI).into(ivUserImg);

                    tvUsername.setText(dataSnapshot.child("first name").getValue(String.class) + " " + dataSnapshot.child("last name").getValue(String.class));
                    tvAge.setText(dataSnapshot.child("age").getValue(String.class));
                    tvEmail.setText(dataSnapshot.child("email").getValue(String.class));
                    tvPhone.setText(dataSnapshot.child("phone").getValue(String.class));
                }

                public void onCancelled(DatabaseError databaseError) {
                }
            });
        } else {
            Glide.with(getApplicationContext()).load(imgGlobalURI).into(ivUserImg);
            tvUsername.setText(name);
            tvAge.setText(age);
            tvEmail.setText(email);
            tvPhone.setText(phone);
        }
        ivConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                uploadImg(userImgLocalUri);
                ivConfirm.setVisibility(View.GONE);
                ivIgnore.setVisibility(View.GONE);
                nothing.setVisibility(View.GONE);
            }
        });
        ivIgnore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Glide.with(getApplicationContext()).load(imgGlobalURI).into(ivUserImg);
                ivConfirm.setVisibility(View.GONE);
                ivIgnore.setVisibility(View.GONE);
                nothing.setVisibility(View.GONE);
            }
        });
    }

    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.nav_change_user_img) {
            startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI), RESULT_LOAD_IMAGE);
        }
        if (id == R.id.nav_change_password) {
            startActivity(new Intent(MainActivity.this, ChangePasswordActivity.class));
        }
        if (id == R.id.nav_about) {
            startActivity(new Intent(MainActivity.this, CoInfoActivity.class));
        }
        if (id == R.id.nav_contact_us) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:16602"));
            startActivity(intent);
        }
        if (id == R.id.nav_logout) {
            logout();
        }
        return true;
    }

    public void logout() {
        databaseReference.child("User Info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("last diagnosis").setValue("-1");
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void uploadImg(Uri userImgLocalUri2) {
        if (userImgLocalUri2 != null) {
            loading.startLoading();
            storageReference=FirebaseStorage.getInstance().getReference().child("User Image").child(System.currentTimeMillis() + "." + getFileExe(userImgLocalUri));
            uploadTask = storageReference.putFile(userImgLocalUri);
            uploadTask.continueWithTask(new Continuation() {
                public Object then(Task task) throws Exception {
                    if (task.isComplete()) {
                        return storageReference.getDownloadUrl();
                    }
                    throw task.getException();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                public void onComplete(Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        userImgGlobalUri = downloadUri.toString();
                        databaseReference.child("User Info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("user image").setValue(userImgGlobalUri);
                        loading.dismissLoading();
                    } else {
                        Toast.makeText(MainActivity.this, "pp:Upload is Failed", Toast.LENGTH_SHORT).show();
                        loading.dismissLoading();
                    }

                }
            });
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == -1 && data != null) {
            Uri data2 = data.getData();
            userImgLocalUri = data2;
            ivUserImg.setImageURI(data2);
            ivConfirm.setVisibility(View.VISIBLE);
            ivIgnore.setVisibility(View.VISIBLE);
            nothing.setVisibility(View.VISIBLE);
        }
    }

    public String getFileExe(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().getType(uri));
    }


}
