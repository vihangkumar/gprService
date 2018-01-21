package com.incomm.wmp.gprServices.outbound.ingo.requests;


import javax.validation.constraints.NotNull;

/**
 * @author dvontela
 */
public class IngoFindCustomerRequest extends BaseRequest {

    @NotNull
    private String ssn;
    @NotNull
    private String dateOfBirth;

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
