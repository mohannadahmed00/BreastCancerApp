package com.example.myhello.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.myhello.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.example.myhello.Classes.Loading;

public class UpLoadImgFragment extends Fragment {

    static int RESULT_LOAD_IMAGE = 1;
    UploadTask uploadTask;
    String imgDownloadUri,userID;
    Uri imgUri;
    ImageView ivAddImg;
    ImageView ivCancer;
    Button btnTest;
    Loading loading;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    long q1,q2;



    public UpLoadImgFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_up_load_img, container, false);
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        this.userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.loading = new Loading(getActivity());
        this.ivAddImg = view.findViewById(R.id.iv_add_image_activity_main);
        this.btnTest = view.findViewById(R.id.btn_test_activity_main);
        this.ivCancer = view.findViewById(R.id.iv_cancer_activity_main);


        this.ivAddImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });
        btnTest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                databaseReference.child("Queue1").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        q1=dataSnapshot.getChildrenCount();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                databaseReference.child("Queue2").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        q2=dataSnapshot.getChildrenCount();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                if (q1 <= q2){
                    uploadImg("Queue1");
                }else {
                    uploadImg("Queue2");
                }


            }
        });



        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            public void handleOnBackPressed() {
                if (imgUri != null) {
                    imgUri = null;
                    ivAddImg.setVisibility(View.VISIBLE);
                    ivCancer.setVisibility(View.GONE);
                } else {
                    getActivity().finish();
                }
            }
        });
        return view;
    }

    private void uploadImg(String q) {
        final String queue=q;
        if (this.imgUri != null) {
            loading.startLoading();
            storageReference=FirebaseStorage.getInstance().getReference().child("Cancer Image").child(System.currentTimeMillis() + "." + getFileExe(imgUri));
            uploadTask = storageReference.putFile(imgUri);
            uploadTask.continueWithTask(new Continuation() {
                public Object then(Task task) throws Exception {
                    if (task.isComplete()) {
                        return storageReference.getDownloadUrl();
                    }
                    throw task.getException();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                public void onComplete(Task<Uri> task) {
                    if (task.isSuccessful()) {
                        imgDownloadUri = task.getResult().toString();
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        Date date = new Date();
                        String time = formatter.format(date);
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("user id", userID);
                        map.put("cancer image", imgDownloadUri);
                        map.put("enhancement", "");
                        map.put("segmentation","");
                        map.put("diagnosis", "-1");
                        map.put("upload date", time);

                        String pushID = databaseReference.child("Cancer Sample").push().getKey();
                        databaseReference.child("Cancer Sample").child(pushID).setValue(map);
                        databaseReference.child(queue).push().setValue(pushID);

                    } else {
                        Toast.makeText(getActivity(), "Upload is Failed", Toast.LENGTH_SHORT).show();
                    }
                    loading.dismissLoading();
                }
            });
            return;
        }
        Toast.makeText(getActivity(), "Please select image", Toast.LENGTH_SHORT).show();
    }

    private String getFileExe(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getActivity().getContentResolver().getType(uri));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == -1 && data != null) {
            imgUri = data.getData();
            ivCancer.setImageURI(imgUri);
            ivCancer.setVisibility(View.VISIBLE);
            this.ivAddImg.setVisibility(View.GONE);
        }
    }
}
