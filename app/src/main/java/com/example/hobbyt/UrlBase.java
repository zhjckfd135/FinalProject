package com.example.hobbyt;

public final class UrlBase {
    public final static String HOME = "http://192.168.56.1:9080/";
    public final static String REGISTER_NEW_USER = HOME + "api/user/registerNewUser";
    public final static String TEST = HOME + "api/test";
    public final static String FIND_COUNT_USER_BY_EMAIL = HOME + "api/user/userIsExists";
    public final static String LOGIN_USER = HOME + "api/user/loginUser";
    public final static String LOGIN_TOKEN = HOME + "api/user/loginToken";
    public final static String ADD_RECORD = HOME + "api/records/newRecord";
    public final static String ADD_PRIMARY_TO_RECORD = HOME + "api/records/addPrimary";
    public final static String GET_RECORD = HOME + "api/records/getRecords";
    public final static String UPDATE_RECORD = HOME + "api/records/updateRecords";
    public final static String GET_USERS = HOME + "api/users";
}
