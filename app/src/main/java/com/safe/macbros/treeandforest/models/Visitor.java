package com.safe.macbros.treeandforest.models;

public class Visitor {
    private String id;
    private String name;
    private String medicalAllergy;
    private String email;
    private String imageOnlinePath;
    private String tailgateId;
    private String imageOfflinePath;
    private String signOfflinePath;
    private String signOnlinePath;
    private String medicalRequirements;

    public Visitor(){

    }

    public Visitor(String id, String name, String medicalAllergy, String email, String imageOnlinePath, String tailgateId, String imageOfflinePath, String signOfflinePath, String signOnlinePath, String medicalRequirements) {
        this.id = id;
        this.name = name;
        this.medicalAllergy = medicalAllergy;
        this.email = email;
        this.imageOnlinePath = imageOnlinePath;
        this.tailgateId = tailgateId;
        this.imageOfflinePath = imageOfflinePath;
        this.signOfflinePath = signOfflinePath;
        this.signOnlinePath = signOnlinePath;
        this.medicalRequirements = medicalRequirements;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMedicalAllergy() {
        return medicalAllergy;
    }

    public void setMedicalAllergy(String medicalAllergy) {
        this.medicalAllergy = medicalAllergy;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageOnlinePath() {
        return imageOnlinePath;
    }

    public void setImageOnlinePath(String imageOnlinePath) {
        this.imageOnlinePath = imageOnlinePath;
    }

    public String getTailgateId() {
        return tailgateId;
    }

    public void setTailgateId(String tailgateId) {
        this.tailgateId = tailgateId;
    }

    public String getImageOfflinePath() {
        return imageOfflinePath;
    }

    public void setImageOfflinePath(String imageOfflinePath) {
        this.imageOfflinePath = imageOfflinePath;
    }

    public String getSignOfflinePath() {
        return signOfflinePath;
    }

    public void setSignOfflinePath(String signOfflinePath) {
        this.signOfflinePath = signOfflinePath;
    }

    public String getSignOnlinePath() {
        return signOnlinePath;
    }

    public void setSignOnlinePath(String signOnlinePath) {
        this.signOnlinePath = signOnlinePath;
    }

    public String getMedicalRequirements() {
        return medicalRequirements;
    }

    public void setMedicalRequirements(String medicalRequirements) {
        this.medicalRequirements = medicalRequirements;
    }
}
