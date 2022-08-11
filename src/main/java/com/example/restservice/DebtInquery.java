package com.example.restservice;

public class DebtInquery {
    private final String personalCode;
    private String decision;
    private long amount;

    private Integer period;

    private double score;

    private String message;
    public DebtInquery(String personalCode, String decision, long amount, Integer period, double score, String message){
        this.personalCode = personalCode;
        this.decision = decision;
        this.amount = amount;
        this.period = period;
        this.score = score;
        this.message = message;
    }

    public String getPersonalCode(){
        return personalCode;
    }

    public String getDecision(){
        return decision;
    }

    public long getAmount(){
        return amount;
    }

    public Integer getPeriod(){
        return period;
    }

    public double getScore(){
        return score;
    }

    public String getMessage(){ return message;}

    public void setDecision(String decision){
        this.decision = decision;
    }

    public void setAmount(long amount){
        this.amount = amount;
    }

    public void setPeriod(Integer period){
        this.period = period;
    }

    public void setScore(double score) {this.score = score;}

    public void setMessage(String message) {this.message = message;}
}
