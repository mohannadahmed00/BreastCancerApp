package com.example.myhello.Classes;

public class Diagnosis {
    private String date;
    private String diagnosis;
    private String img,enhanced,segmented;

    public Diagnosis(String img, String diagnosis, String date,String enhanced,String segmented) {
        this.date = date;
        this.diagnosis = diagnosis;
        this.img = img;
        this.enhanced=enhanced;
        this.segmented=segmented;
    }

    public String getEnhanced() {
        return enhanced;
    }

    public String getSegmented() {
        return segmented;
    }

    public String getDate() {
        return this.date;
    }

    public String getDiagnosis() {
        return this.diagnosis;
    }

    public String getImg() {
        return this.img;
    }
}
