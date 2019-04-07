package org.chibamuio.moneytransfer.util;

public class AppConstants {

    public final static int GENERAL_ERROR_CODE = 4000;
    public final static int SAME_ACC_TRANSFER = 4001;
    public final static int INSUFFICIENT_FUNDS = 4002;
    public final static int INVALID_INPUT_DATA = 4003;
    public final static int NOT_FOUND_CUSTOMER = 4004;
    public final static int NOT_FOUND_ACCOUNT = 4005;
    public final static int NONE_EMPTY_ACCOUNT = 4006;
    public final static int NATIONAL_ID_NUMBER_LENGTH = 13;
    public final static String NATIONA_ID_NUMBER_ERR_MSG = "National identity number must be exactly 13 numbers";
    public final static String ACCOUNT_NUMBER_VALIDATION_MSG = "Account number should be numeric and positive";
    public static final String AMOUNT_VALIDATION_MSG = "Amount should be positive number";
    public static String HOST = "http://localhost/api/";
    public static int PORT = 8888;
}
