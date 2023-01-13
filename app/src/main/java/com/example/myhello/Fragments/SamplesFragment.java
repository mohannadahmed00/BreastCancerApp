package com.example.myhello.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.myhello.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.example.myhello.Adapters.DiagnosisAdapter;
import com.example.myhello.Classes.Diagnosis;


    public class SamplesFragment extends Fragment {

        Dialog dialog;
        DiagnosisAdapter diagnosisAdapter;
        ArrayList<Diagnosis> diagnosisList;
        TextView tvNoDiagnosis;
        ImageView iv_enhanced,iv_segmented;

        public SamplesFragment() {
            // Required empty public constructor
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_samples, container, false);

            dialog=new Dialog(getActivity());
            dialog.setContentView(R.layout.e_s);
            iv_enhanced=dialog.findViewById(R.id.iv_enhanced);
            iv_segmented=dialog.findViewById(R.id.iv_segmented);


            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Cancer Sample");
            final ListView lvDiagnosis = view.findViewById(R.id.lv_diagnosis_fragment_diagnosis);
            tvNoDiagnosis = view.findViewById(R.id.tv_no_diagnosis_fragment_diagnosis);
            diagnosisList = new ArrayList<>();
            diagnosisAdapter = new DiagnosisAdapter(getContext(), diagnosisList);
            lvDiagnosis.setAdapter(diagnosisAdapter);
            databaseReference.addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    diagnosisList.clear();
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(snap.child("user id").getValue(String.class))) {
                            diagnosisList.add(new Diagnosis(snap.child("cancer image").getValue(String.class), snap.child("diagnosis").getValue(String.class), snap.child("upload date").getValue(String.class),snap.child("enhancement").getValue(String.class),snap.child("segmentation").getValue(String.class)));
                            diagnosisAdapter.notifyDataSetChanged();
                        }
                    }
                if (diagnosisList.size() != 0) {
                    tvNoDiagnosis.setVisibility(View.GONE);
                } else {
                    tvNoDiagnosis.setVisibility(View.VISIBLE);
                }
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });

        lvDiagnosis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Diagnosis diagnosis= (Diagnosis) parent.getAdapter().getItem(position);
                String eLink=diagnosis.getEnhanced(),sLink=diagnosis.getSegmented();
                if (eLink.equals("") || sLink.equals("")){
                    Toast.makeText(getActivity(), "wait..", Toast.LENGTH_SHORT).show();
                }else {
                    dialog.show();
                    Glide.with(getActivity()).load(diagnosis.getEnhanced()).into(iv_enhanced);
                    Glide.with(getActivity()).load(diagnosis.getSegmented()).into(iv_segmented);


                }

            }
        });


        return view;
    }
}
