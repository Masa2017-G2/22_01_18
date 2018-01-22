package com.sheygam.masa_g2_22_01_18;

/**
 * Created by gregorysheygam on 22/01/2018.
 */

public class Auth {
    private String email;
    private String password;

    public Auth() {
    }

    public Auth(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Auth{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
