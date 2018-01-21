package com.incomm.wmp.gprServices.response;

public class AlertSettingsResponse {

    protected AlertInfo alertInfo;

    public AlertInfo getAlertInfo() {
        return alertInfo;
    }

    public void setAlertInfo(AlertInfo alertInfo) {
        this.alertInfo = alertInfo;
    }

    public  static AlertInfo  parseAlertSettingsInfo(String alertSettingsData) {
        AlertInfo alertSettings = null;
        if (alertSettingsData != null) {
            alertSettings = new AlertInfo();
            String[] alertArray = alertSettingsData.split("\\|\\|");
            if (alertArray != null && alertArray.length > 0) {
                for (String alertData: alertArray) {
                    String[] alertDataArray = alertData.split("~", -1);
                    if (alertDataArray != null && alertDataArray.length > 0) {
                        alertSettings = handleAlertByName(alertSettings, alertDataArray);
                    }
                }
            }
        }
       // setAlertInfo((List<AlertInfo>) alertSettings);
        return alertSettings;
    }



    private static AlertInfo handleAlertByName(AlertInfo alertSettings, String[] alertDataArray) {
        String alertName = alertDataArray[0];
        if (alertName != null && AlertDetails.containsValidAlertName(alertName)) {
            AlertDetails.Setting alert= AlertDetails.Setting.valueOf(alertName);
            switch(alert) {
                case DAILY_BAL_ALERT:
                    alertSettings = handleDailyBalanceAlert(alertSettings, alertDataArray);
                    break;

                case LOW_BAL_ALERT:
                    alertSettings = handleLowBalanceAlert(alertSettings, alertDataArray);
                    break;

                case HIGH_AUTH_ALERT:
                    alertSettings = handleHighAuthAmountAlert(alertSettings, alertDataArray);
                    break;

                case LOAD_ALERT:
                    alertSettings = handleLoadOrCreditAlert(alertSettings, alertDataArray);
                    break;

                case NEG_BAL_ALERT:
                    alertSettings = handleNegativeBalanceAlert(alertSettings, alertDataArray);
                    break;

                case INSUFFICIENT_FUND_ALERT:
                    alertSettings = handleInsufficientFundsAlert(alertSettings, alertDataArray);
                    break;

                case INCORRECT_PIN_ALERT:
                    alertSettings = handleIncorrectPinAlert(alertSettings, alertDataArray);
                    break;
            }
        }

        return alertSettings;
    }

    private static AlertInfo handleLowBalanceAlert(AlertInfo alertSettings, String[] alertDataArray) {
        String type = alertDataArray[1];
        if (type != null && AlertDetails.containsValidAlertType(type)) {
            AlertDetails.Type alertType = AlertDetails.Type.valueOf(type);
            String status = alertDataArray[2];
            if (status != null && AlertDetails.containsValidAlertStatus(status)) {
                AlertDetails.Status alertStatus = AlertDetails.Status.valueOf(status);
                switch (alertType) {
                    case EMAIL:
                        alertSettings.setLowBalanceEmailAlert(getAlertStatusBooleanVal(alertStatus));
                        break;
                    case SMS:
                        alertSettings.setLowBalanceSMSAlert(getAlertStatusBooleanVal(alertStatus));
                        break;
                    case BOTH:
                        alertSettings.setLowBalanceEmailAlert(getAlertStatusBooleanVal(alertStatus));
                        alertSettings.setLowBalanceSMSAlert(getAlertStatusBooleanVal(alertStatus));
                }

                String amount = alertDataArray[3];
                if (amount != null &&  amount.trim().length() > 0  && getAlertStatusBooleanVal(alertStatus)) {
                    alertSettings.setLowBalanceAmount(amount.trim());
                }

            }
        }

        return alertSettings;
    }


    private static AlertInfo handleDailyBalanceAlert(AlertInfo alertSettings, String[] alertDataArray) {
        String type = alertDataArray[1];
        if (type != null && AlertDetails.containsValidAlertType(type)) {
            AlertDetails.Type alertType = AlertDetails.Type.valueOf(type);
            String status = alertDataArray[2];
            if (status != null && AlertDetails.containsValidAlertStatus(status)) {
                AlertDetails.Status alertStatus = AlertDetails.Status.valueOf(status);
                switch (alertType) {
                    case EMAIL:
                        alertSettings.setDailyBalanceEmailAlert(getAlertStatusBooleanVal(alertStatus));
                        break;
                    case SMS:
                        alertSettings.setDailyBalanceSMSAlert(getAlertStatusBooleanVal(alertStatus));
                        break;
                    case BOTH:
                        alertSettings.setDailyBalanceEmailAlert(getAlertStatusBooleanVal(alertStatus));
                        alertSettings.setDailyBalanceSMSAlert(getAlertStatusBooleanVal(alertStatus));
                }
                //NOTE: Begin & End time are NOT found in the retrieve alert settings response.
            }
        }

        return alertSettings;
    }

    private static AlertInfo handleIncorrectPinAlert(AlertInfo alertSettings, String[] alertDataArray) {
        String type = alertDataArray[1];
        if (type != null && AlertDetails.containsValidAlertType(type)) {
            AlertDetails.Type alertType = AlertDetails.Type.valueOf(type);
            String status = alertDataArray[2];
            if (status != null && AlertDetails.containsValidAlertStatus(status)) {
                AlertDetails.Status alertStatus = AlertDetails.Status.valueOf(status);
                switch (alertType) {
                    case EMAIL:
                        alertSettings.setIncorrectPinUsageEmailAlert(getAlertStatusBooleanVal(alertStatus));
                        break;
                    case SMS:
                        alertSettings.setIncorrectPinUsageSMSAlert(getAlertStatusBooleanVal(alertStatus));
                        break;
                    case BOTH:
                        alertSettings.setIncorrectPinUsageEmailAlert(getAlertStatusBooleanVal(alertStatus));
                        alertSettings.setIncorrectPinUsageSMSAlert(getAlertStatusBooleanVal(alertStatus));
                }
            }
        }

        return alertSettings;
    }

    private static AlertInfo handleInsufficientFundsAlert(AlertInfo alertSettings, String[] alertDataArray) {
        String type = alertDataArray[1];
        if (type != null && AlertDetails.containsValidAlertType(type)) {
            AlertDetails.Type alertType = AlertDetails.Type.valueOf(type);
            String status = alertDataArray[2];
            if (status != null && AlertDetails.containsValidAlertStatus(status)) {
                AlertDetails.Status alertStatus = AlertDetails.Status.valueOf(status);
                switch (alertType) {
                    case EMAIL:
                        alertSettings.setInsufficientFundsEmailAlert(getAlertStatusBooleanVal(alertStatus));
                        break;
                    case SMS:
                        alertSettings.setInsufficientFundsSMSAlert(getAlertStatusBooleanVal(alertStatus));
                        break;
                    case BOTH:
                        alertSettings.setInsufficientFundsEmailAlert(getAlertStatusBooleanVal(alertStatus));
                        alertSettings.setInsufficientFundsSMSAlert(getAlertStatusBooleanVal(alertStatus));
                }
            }
        }

        return alertSettings;
    }

    private static AlertInfo handleNegativeBalanceAlert(AlertInfo alertSettings, String[] alertDataArray) {
        String type = alertDataArray[1];
        if (type != null && AlertDetails.containsValidAlertType(type)) {
            AlertDetails.Type alertType = AlertDetails.Type.valueOf(type);
            String status = alertDataArray[2];
            if (status != null && AlertDetails.containsValidAlertStatus(status)) {
                AlertDetails.Status alertStatus = AlertDetails.Status.valueOf(status);
                switch (alertType) {
                    case EMAIL:
                        alertSettings.setNegativeBalanceEmailAlert(getAlertStatusBooleanVal(alertStatus));
                        break;
                    case SMS:
                        alertSettings.setNegativeBalanceSMSAlert(getAlertStatusBooleanVal(alertStatus));
                        break;
                    case BOTH:
                        alertSettings.setNegativeBalanceEmailAlert(getAlertStatusBooleanVal(alertStatus));
                        alertSettings.setNegativeBalanceSMSAlert(getAlertStatusBooleanVal(alertStatus));
                }
            }
        }

        return alertSettings;
    }

    private static AlertInfo handleLoadOrCreditAlert(AlertInfo alertSettings, String[] alertDataArray) {
        String type = alertDataArray[1];
        if (type != null && AlertDetails.containsValidAlertType(type)) {
            AlertDetails.Type alertType = AlertDetails.Type.valueOf(type);
            String status = alertDataArray[2];
            if (status != null && AlertDetails.containsValidAlertStatus(status)) {
                AlertDetails.Status alertStatus = AlertDetails.Status.valueOf(status);
                switch (alertType) {
                    case EMAIL:
                        alertSettings.setLoadOrCreditEmailAlert(getAlertStatusBooleanVal(alertStatus));
                        break;
                    case SMS:
                        alertSettings.setLoadOrCreditSMSAlert(getAlertStatusBooleanVal(alertStatus));
                        break;
                    case BOTH:
                        alertSettings.setLoadOrCreditEmailAlert(getAlertStatusBooleanVal(alertStatus));
                        alertSettings.setLoadOrCreditSMSAlert(getAlertStatusBooleanVal(alertStatus));
                }
            }
        }

        return alertSettings;
    }

    private static AlertInfo handleHighAuthAmountAlert(AlertInfo alertSettings, String[] alertDataArray) {
        String type = alertDataArray[1];
        if (type != null && AlertDetails.containsValidAlertType(type)) {
            AlertDetails.Type alertType = AlertDetails.Type.valueOf(type);
            String status = alertDataArray[2];
            if (status != null && AlertDetails.containsValidAlertStatus(status)) {
                AlertDetails.Status alertStatus = AlertDetails.Status.valueOf(status);
                switch (alertType) {
                    case EMAIL:
                        alertSettings.setHighAuthAmountEmailAlert(getAlertStatusBooleanVal(alertStatus));
                        break;
                    case SMS:
                        alertSettings.setHighAuthAmountSMSAlert(getAlertStatusBooleanVal(alertStatus));
                        break;
                    case BOTH:
                        alertSettings.setHighAuthAmountEmailAlert(getAlertStatusBooleanVal(alertStatus));
                        alertSettings.setHighAuthAmountSMSAlert(getAlertStatusBooleanVal(alertStatus));
                }

                String amount = alertDataArray[3];
                if (amount != null &&  amount.trim().length() > 0  && getAlertStatusBooleanVal(alertStatus)) {
                    alertSettings.setHighAuthAmount(amount.trim());
                }

            }
        }
        return alertSettings;
    }

    private static boolean getAlertStatusBooleanVal(AlertDetails.Status alertStatus) {
        switch(alertStatus) {
            case ON:
                return true;
            case OFF:
                return false;
        }
        return false;
    }


}
