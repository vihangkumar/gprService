package com.incomm.wmp.gprServices.response;

public class AlertDetails {

	public enum Setting {
		DAILY_BAL_ALERT, 
		LOW_BAL_ALERT, 
		NEG_BAL_ALERT, 
		LOAD_ALERT, 
		HIGH_AUTH_ALERT, 
		INSUFFICIENT_FUND_ALERT, 
		INCORRECT_PIN_ALERT;

	}

	public enum Type {
		EMAIL, 
		SMS, 
		BOTH;
	}
	
	public enum Status {
		ON,
		OFF;
	}
	
	/**
	 * This method is to check if the alert name is valid.
	 * @param alertName
	 * @return
	 */
	public static boolean containsValidAlertName(String alertName) {
	    for (Setting setting : Setting.values()) {
	        if (setting.name().equals(alertName)) {
	            return true;
	        }
	    }
	    return false;
	}
	
	/**
	 * This method is to check if the alert type is valid.
	 * @param
	 * @return
	 */
	public static boolean containsValidAlertType(String alertType) {
	    for (Type type : Type.values()) {
	        if (type.name().equals(alertType)) {
	            return true;
	        }
	    }
	    return false;
	}
	
	/**
	 * This method is to check if the alert status is valid.
	 * @param 
	 * @return
	 */
	public static boolean containsValidAlertStatus(String alertStatus) {
	    for (Status status : Status.values()) {
	        if (status.name().equals(alertStatus)) {
	            return true;
	        }
	    }
	    return false;
	}
	

	

}