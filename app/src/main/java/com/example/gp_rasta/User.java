package com.example.gp_rasta;

public class User {

    String firstName, lastName, Email,PhoneNo;
    int id;



    public User()
   {

   }
    public User(String firstName,String lastNmae,String Email,String PhoneNo,int id)
    {
        this.firstName=firstName;
        this.lastName=lastNmae;
        this.Email=Email;
        this.PhoneNo=PhoneNo;
        this.id=id;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        Email = email;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
