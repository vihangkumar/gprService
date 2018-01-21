package com.incomm.wmp.gprServices.request;

public class UserAccountInfoReq {

    private String username;
    private String password;
    private String confirmPassword;
    private String securityQuestionOne;
    private String securityAnswerOne;
    private String securityQuestionTwo;
    private String securityAnswerTwo;
    private String securityQuestionThree;
    private String securityAnswerThree;
    private String request;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getSecurityQuestionOne() {
        return securityQuestionOne;
    }

    public void setSecurityQuestionOne(String securityQuestionOne) {
        this.securityQuestionOne = securityQuestionOne;
    }

    public String getSecurityAnswerOne() {
        return securityAnswerOne;
    }

    public void setSecurityAnswerOne(String securityAnswerOne) {
        this.securityAnswerOne = securityAnswerOne;
    }

    public String getSecurityQuestionTwo() {
        return securityQuestionTwo;
    }

    public void setSecurityQuestionTwo(String securityQuestionTwo) {
        this.securityQuestionTwo = securityQuestionTwo;
    }

    public String getSecurityAnswerTwo() {
        return securityAnswerTwo;
    }

    public void setSecurityAnswerTwo(String securityAnswerTwo) {
        this.securityAnswerTwo = securityAnswerTwo;
    }

    public String getSecurityQuestionThree() {
        return securityQuestionThree;
    }

    public void setSecurityQuestionThree(String securityQuestionThree) {
        this.securityQuestionThree = securityQuestionThree;
    }

    public String getSecurityAnswerThree() {
        return securityAnswerThree;
    }

    public void setSecurityAnswerThree(String securityAnswerThree) {
        this.securityAnswerThree = securityAnswerThree;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
