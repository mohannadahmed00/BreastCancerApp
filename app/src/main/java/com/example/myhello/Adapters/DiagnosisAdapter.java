package com.example.myhello.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myhello.R;

import java.util.ArrayList;

import com.example.myhello.Classes.Diagnosis;

public class DiagnosisAdapter extends ArrayAdapter {
    TextView tvDate,tvDiagnosis;
    public DiagnosisAdapter(Context context, ArrayList<Diagnosis> diagnoses) {
        super(context, 0, diagnoses);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View convertView2 = LayoutInflater.from(getContext()).inflate(R.layout.diagnosis, parent, false);
        Diagnosis diagnosis = (Diagnosis) getItem(position);
        tvDate = convertView2.findViewById(R.id.tv_date_diagnosis);
        tvDiagnosis = convertView2.findViewById(R.id.tv_diagnosis_diagnosis);
        Glide.with(convertView2.getContext().getApplicationContext()).load(diagnosis.getImg()).into((ImageView) convertView2.findViewById(R.id.iv_cancer_diagnosis));
        if (diagnosis.getDiagnosis().equals("1")) {
            tvDiagnosis.setText("not cancer");
        } else if (diagnosis.getDiagnosis().equals("0")) {
            tvDiagnosis.setText("cancer");
        } else {
            tvDiagnosis.setText("in progress...");
        }
        tvDate.setText(diagnosis.getDate());
        return convertView2;
    }
}
