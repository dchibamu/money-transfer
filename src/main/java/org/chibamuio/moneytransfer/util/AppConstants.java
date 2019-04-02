package org.chibamuio.moneytransfer.util;

public class AppConstants {

    public final static int GENERAL_ERROR_CODE = 4000;
    public final static int SAME_ACC_TRANSFER = 4001;
    public final static int NATIONAL_ID_NUMBER_LENGTH = 13;
    public final static String NATIONA_ID_NUMBER_ERR_MSG = "National identity number must be exactly 13 numbers";
    public final static String ACCOUNT_NUMBER_VALIDATION_MSG = "Account number should be numeric and positive";
    public static final String AMOUNT_VALIDATION_MSG = "Amount should be positive number";
    public static String HOST = "http://localhost/";
    public static int PORT = 8888;
}
