package com.example.Formation.certificat.service.event;

public class EvaluationRequest {
    private String enrollmentId;
    private int score;

    public EvaluationRequest() {}

    public String getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(String enrollmentId) { this.enrollmentId = enrollmentId; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
}