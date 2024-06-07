package com.application.intercom.utils;

public class Validator {
    public final static String NUMBER_PATTERN = "^[0-9]$";
    public final static String ALL_PATTERN = "^[a-zA-Z0-9!@#$&()\\\\-`.+,/\\\"]*$";
    public final static String ALPHABET_PATTERN = "^[a-zA-Z\\s]+$ ";
    public final static String EMAIL_PATTERN = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}";
    public final static String PHONE_NO_PATTERN = "^(?:(?:\\+|0{0,2})91(\\s*[\\ -]\\s*)?|[0]?)?[789]\\d{9}|(\\d[ -]?){10}\\d$";

    public static boolean isValidNickname(String nickname) {
        if (nickname == null) nickname = "";
        String regexSt = "^[a-zA-Z0-9]+([a-zA-Z0-9](_|.| )[a-zA-Z0-9])*[a-zA-Z0-9]+$"; //with digit
        return nickname.matches(regexSt);
    }

    public static boolean isAlphaNumeric(String s) {
        String pattern = "^[a-zA-Z0-9]*$";
        return s.matches(pattern);
    }
    public static boolean istest(String s) {
        String pattern = ALPHABET_PATTERN;
        return s.matches(pattern);
    }

    public static boolean isValidLastName(String lastName) {
        String regexStr = "^$|\\s+";
        String regexSt = "^[a-zA-Z\\s]+$";
        return lastName.matches(regexSt) || lastName.matches(regexStr);
    }

    public static boolean isValidFirstName(String name) {
        String regexSt = "^[a-zA-Z\\s]+$";
        return name != null && name.length() > 2 && name.matches(regexSt);
    }

    public static boolean isValidFullName(String name) {
        String regexSt = "^[a-z ,.'-]+$";
        //return name != null && name.length() > 2 && name.matches(regexSt);
        return name != null && name.length() > 2 ;
    }

    public static boolean isEmpty(String name) {
        String regexSt = "^[a-z ,.'-]+$";
        //return name != null && name.length() > 2 && name.matches(regexSt);
        return  name.length() == 0 ;
    }

    public static boolean isValidPhone(String phone) {
        //String regexStr = "^(?:(?:\\+|0{0,2})91(\\s*[\\ -]\\s*)?|[0]?)?[789]\\d{9}|(\\d[ -]?){9}\\d$";
        return (phone.length() >= 8&&phone.length()<=13);
    }

    public static boolean isValidPhoneAllCountry(String phone){
        return (phone.length() >= 8&&phone.length()<=13);
    }

    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}";
        return email.matches(EMAIL_PATTERN);
    }

    public static boolean isValidPAN(String pan) {
        String regexStr = "[A-Za-z]{5}\\d{4}[A-Za-z]{1}";
        return pan.matches(regexStr);
    }

    public static boolean isValidIFSC(String ifsc) {
        String regexStr = "^[A-Za-z]{4}\\d{7}$";
        return ifsc.matches(regexStr);
    }

    public static boolean isValidPassword(String testString) {
        return (testString.length() >= 7&&testString.length()<=16);
    }

    public static boolean isValidAlias(String value) {
        String regexSt = "^[a-zA-Z\\s]+$";
        return value != null && value.length() > 2 && value.matches(regexSt);
    }
}

