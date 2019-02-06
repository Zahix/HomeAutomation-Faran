package com.example.zahid.homeautomation;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zahid.homeautomation.Utill.Common;
import com.example.zahid.homeautomation.Utill.Tools;
import com.google.firebase.auth.FirebaseAuth;
import com.mikhaellopez.circularimageview.CircularImageView;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProfileSettingActivity extends AppCompatActivity {

    TextView tv_name, tv_view_edit_profile;
    CircularImageView civ_avatar;
    LinearLayout ll_rateapp;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);
//        Tools.setSystemBarColor(this, android.R.color.white);


        mAuth = FirebaseAuth.getInstance();
        iniUiComponents();
        headerComponents();

        ll_rateapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(ProfileSettingActivity.this,RateAppActivity.class);
            startActivity(intent);
            }
        });
        tv_view_edit_profile.setPaintFlags(tv_view_edit_profile.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        if (Common.profile != null) {
            if (Common.profile.getName() != null) {
                tv_name.setText(Common.profile.getName());
            }
            if (Common.profile.getGender() != null) {
                if (Common.profile.getGender().equals("Female") || Common.profile.getGender().equals("Other")) {
                    civ_avatar.setImageResource(R.drawable.girl);
                }
            }
        }

        tv_view_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileSettingActivity.this, ProfileViewActivity.class);
                startActivity(intent);
            }
        });
    }


    private void headerComponents() {

        View backPressed = findViewById(R.id.back_btn);
        backPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        View homeScreen = findViewById(R.id.imageviewHome);
        homeScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileSettingActivity.this, IndexActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        View logout = findViewById(R.id.powerImage);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog();
            }
        });
    }

    private void logoutDialog() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setCanceledOnTouchOutside(false);
        sweetAlertDialog.setTitleText("Logout");
        sweetAlertDialog.setContentText("Are you sure you want to Logout?");
        sweetAlertDialog.setConfirmText("Yes");
        sweetAlertDialog.setCancelText("No");
        sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.cancel();
            }
        });
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {

                mAuth.getInstance().signOut();
                Intent intent = new Intent(ProfileSettingActivity.this, FirstPageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                sweetAlertDialog.cancel();
                startActivity(intent);
                finish();
            }
        });
        sweetAlertDialog.show();

    }

    private void iniUiComponents() {
        ll_rateapp = (LinearLayout) findViewById(R.id.ll_rateapp);
        civ_avatar = (CircularImageView) findViewById(R.id.civ_avatar);
        tv_view_edit_profile = (TextView) findViewById(R.id.tv_view_edit_profile);
        tv_name = (TextView) findViewById(R.id.tv_name);
    }
}
