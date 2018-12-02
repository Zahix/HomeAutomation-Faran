package com.example.zahid.homeautomation.Utill;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.zahid.homeautomation.Model.Account;
import com.example.zahid.homeautomation.Model.UnitCalOffline;

public class Common {
    public static int month = 0;
    public static boolean monthSizeExceed;
    public static String Account_ID;
    public static final String STR_Account = "account";
    public static final String STR_Units = "mounits";
    public static final String STR_Month = "monthlydevicedata";
    public static boolean From_Main_Page = true;
    public static Account profile;
    public static boolean StateForMac = true;
    public static UnitCalOffline unitCalService;
    public static String offlineRPS;

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
