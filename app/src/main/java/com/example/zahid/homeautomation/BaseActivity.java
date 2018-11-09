package com.example.zahid.homeautomation;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);


    }

    public boolean hasInternet(ConnectivityManager connectivity) {
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    public void sendEmail(FirebaseUser user) {
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
//                Toast.makeText(getApplicationContext(), "Verification Email Sent", Toast.LENGTH_SHORT).show();
                Log.i("Verification", "verification email sent");
            }
        });
    }

    public void sucessDialog(Context context, String contentText, String titleText, FirebaseUser user) {

        if (user != null) {
            new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(user.getEmail())
                    .setContentText(contentText)
                    .setConfirmText("OK")
                    .setConfirmClickListener(null)
                    .show();
        } else {
            new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(titleText)
                    .setContentText(contentText)
                    .setConfirmText("OK")
                    .setConfirmClickListener(null)
                    .show();
        }
    }

    public void warningDialog(Context context, String contentText, String titleText) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(titleText)
                .setContentText(contentText)
                .setConfirmText("OK")
                .setConfirmClickListener(null)
                .show();
    }
    public void errorDialog(Context context, String contentText, String titleText){
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(titleText)
                .setContentText(contentText)
                .setConfirmText("OK")
                .setConfirmClickListener(null)
                .show();

    }
}
