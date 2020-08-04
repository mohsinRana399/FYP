package com.paradigmshift.fyp.Hire.payment.Response;

/**
 * The response model object for the Google Pay tokenisation error
 */
public class GooglePayTokenisationFail {

    private String request_id;
    private String error_type;
    private String[] error_codes;

    public String getRequestId() {
        return request_id;
    }

    public String getErrorType() {
        return error_type;
    }

    public String[] getErrorCodes() {
        return error_codes;
    }
}
