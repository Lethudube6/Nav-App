package com.example.mypocketnavv;

public class newuserdatareg {
    String userid;
    String fname;
    String lname;
    String email;

    public newuserdatareg(){}

    public newuserdatareg(String userid, String fname, String lname, String email) {
        this.userid = userid;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
    }

    public String getUserid() {
        return userid;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getEmail() {
        return email;
    }
}
