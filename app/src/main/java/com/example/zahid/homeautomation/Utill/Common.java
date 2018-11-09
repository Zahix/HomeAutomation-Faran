package com.example.zahid.homeautomation.Utill;

import android.util.Patterns;

public class Common {
    public static int month = 0;
    public static boolean monthSizeExceed;
    public static String Account_ID;
    public static final String STR_Account = "account";
    public static final String STR_Units = "mounits";
    public static final String STR_Month = "monthlydevicedata";
    public static boolean From_Main_Page = true;


    public static String Validation(String username, String email, String password) {

        if (username.isEmpty()) {
            return "required username";
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "vaildate email";
        }

        if (email.isEmpty()) {
            return "required email";
        }

        if (password.isEmpty()) {
            return "required password";
        }

        if (password.length() < 6) {
            return "min password";
        } else {
            return "vaildated";
        }
    }

}
