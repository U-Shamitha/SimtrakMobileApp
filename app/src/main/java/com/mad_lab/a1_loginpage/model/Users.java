package com.mad_lab.a1_loginpage.model;

public class Users {

    public String salutation, applyingFor, firstName, lastName, email, countryCode, phoneNumber, city, pincode;


    public Users() {


    }

    public Users(String salutation, String applyingFor, String firstName, String  lastName, String email, String countryCode, String phoneNumber, String city, String pincode){
       this.salutation = salutation;
       this.applyingFor = applyingFor;
       this.firstName = firstName;
       this.lastName = lastName;
       this.email = email;
       this.countryCode = countryCode;
       this.phoneNumber = phoneNumber;
       this.city = city;
       this.pincode = pincode;
    }


}
