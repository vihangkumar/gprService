package com.incomm.wmp.gprServices.request;

public class UpdatePasswordReq {

    String userName;
    String oldPassword;
    String password;

    public String getUserName() {return userName; }

    public void setUserName(String userName) { this.userName = userName; }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
