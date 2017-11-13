package com.example.victor.latrans.repocitory.local.model;


public class Registration {

    public String first_name;
    public String last_name;
    public String email;
    public String password;

    public Registration(String first_name, String last_name, String email, String password) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
    }

    public Registration(){
    }

}
