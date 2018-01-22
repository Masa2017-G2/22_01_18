package com.sheygam.masa_g2_22_01_18;

/**
 * Created by gregorysheygam on 22/01/2018.
 */

public class Person {
    private String name;
    private String email;
    private String phone;

    private Person(){

    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public static class Builder{
        private String bName;
        private String bEmail;
        private String bPhone;

        public Builder name(String name){
            bName = name;
            return this;
        }

        public Builder phone(String phone){
            bPhone = phone;
            return this;
        }

        public Builder email(String email){
            bEmail = email;
            return this;
        }

        public Person build(){
            Person p = new Person();
            p.name = bName == null ? "John Doe" : bName;
            p.email = bEmail == null ? "johndoe@mail.com" : bEmail;
            p.phone = bPhone == null ? "123456789":bPhone;
            return p;
        }
    }
}
