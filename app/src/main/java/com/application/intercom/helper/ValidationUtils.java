package com.application.intercom.helper;

import android.util.Patterns;

import java.util.regex.Pattern;

public class ValidationUtils {
    public static boolean stringNullValidation(String str){
        return str != null && !str.isEmpty();
    }

    public static boolean mobileValidation(String str){
       return str !=null && str.length()==10 ;
    }

    public static boolean passwordValidation(String str){
        Pattern PASSWORD_PATTERN = Pattern.compile("^" + "(?=.*[0-9])" + "(?=.*[a-z])" + "(?=.*[A-Z])" + "(?=.*[a-zA-Z])" + "(?=.*[@#$%^&+=])" + ".{8,}" + "$");
        return  PASSWORD_PATTERN.matcher(str).matches();
    }

    public static boolean emailValidation(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }

    public static boolean aadharValidation(String aadhar){
       return aadhar != null && aadhar.length() == 12;
    }

    public static boolean panValidation(String panCard){
        return  panCard !=null && panCard.matches("[A-Z]{5}[0-9]{4}[A-Z]{1}") && panCard.length()==10;
    }

    public static boolean couponsCodeValidation(String couponsCode){
        return  couponsCode !=null && couponsCode.matches(  "^[a-zA-Z0-9]+$");
    }

}

