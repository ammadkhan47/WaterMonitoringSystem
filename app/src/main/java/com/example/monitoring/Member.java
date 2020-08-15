package com.example.monitoring;

public class Member {

    private String query,temp,turb,ph,consum;


    public Member(){

    }

    public Member(String query,String temp,String turb,String ph){
        this.query = query;
        this.temp = temp;
        this.turb = turb;
        this.ph = ph;

    }

    public Member(String query,String consum){
        this.consum=consum;
        this.query=query;
    }

    public String getConsum(){
        return consum;
    }

    public void setConsum(String consum){
        this.consum=consum;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String temp) {
        this.query = query;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTurb() {
        return turb;
    }

    public void setTurb(String turb) {
        this.turb = turb;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

  
}
