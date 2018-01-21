package com.incomm.wmp.gprServices.outbound.ingo.requests;


import javax.validation.constraints.NotNull;

/**
 * @author Allen
 */
public class IngoAddSessionAttributesRequest extends BaseRequest{

    @NotNull
    private String ssoToken;

    private String preferredEmail;
    private String[] accountIdFilter;
    private String[] accountNumberFilter;

    public String getSsoToken() {
        return ssoToken;
    }

    public void setSsoToken(String ssoToken) {
        this.ssoToken = ssoToken;
    }

    public String getPreferredEmail() {
        return preferredEmail;
    }

    public void setPreferredEmail(String preferredEmail) {
        this.preferredEmail = preferredEmail;
    }

    public String[] getAccountIdFilter() {
        return accountIdFilter;
    }

    public void setAccountIdFilter(String[] accountIdFilter) {
        this.accountIdFilter = accountIdFilter;
    }

    public String[] getAccountNumberFilter() {
        return accountNumberFilter;
    }

    public void setAccountNumberFilter(String[] accountNumberFilter) {
        this.accountNumberFilter = accountNumberFilter;
    }
}
