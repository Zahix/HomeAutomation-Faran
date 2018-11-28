package com.example.zahid.homeautomation;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zahid.homeautomation.Utill.Common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ProfileViewActivity extends AppCompatActivity {

    TextView txtFN8ame,txtLName,txtEmail,spinnerGender,spinnerDOB,txtOccupation,et_house_no,et_city;
    FirebaseAuth mAuth;
//    ImageView img_back_btn;
    FloatingActionButton fab_edit;
    CircularImageView civ_profile_avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);


        mAuth = FirebaseAuth.getInstance();
        initComponents();
        headerComponents();
        txtFN8ame.setText(Common.profile.getName());
        txtLName.setText(Common.profile.getLname());
        txtEmail.setText(Common.profile.getEmail());
        spinnerGender.setText(Common.profile.getGender());
        spinnerDOB.setText(Common.profile.getDob());
        txtOccupation.setText(Common.profile.getOccupation());
        et_house_no.setText(Common.profile.getAddress());
        et_city.setText(Common.profile.getCity());


        if (Common.profile.getGender().equals("Male")){
            civ_profile_avatar.setImageResource(R.drawable.face);
        }
        else {
            civ_profile_avatar.setImageResource(R.drawable.girl);
        }

        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileViewActivity.this,ProfileEditActivity.class);
                startActivity(intent);
            }
        });
//        img_back_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
    }


    private void headerComponents() {

        View backPressed = findViewById(R.id.back_btn);
        backPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        View homeScreen  = findViewById(R.id.imageviewHome);
        homeScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileViewActivity.this,IndexActivity.class);
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
                Intent intent = new Intent(ProfileViewActivity.this, FirstPageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                sweetAlertDialog.cancel();
                startActivity(intent);
                finish();
            }
        });
        sweetAlertDialog.show();

    }

    private void initComponents() {
        civ_profile_avatar = (CircularImageView) findViewById(R.id.civ_profile_avatar);
        et_house_no = (TextView) findViewById(R.id.et_house_no);
        txtOccupation = (TextView) findViewById(R.id.txtOccupation);
        spinnerDOB = (TextView) findViewById(R.id.spinnerDOB);
        spinnerGender = (TextView) findViewById(R.id.spinnerGender);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtLName = (TextView) findViewById(R.id.txtLName);
        txtFN8ame = (TextView) findViewById(R.id.txtFN8ame);
        et_city = (TextView) findViewById(R.id.et_city);
        fab_edit = (FloatingActionButton) findViewById(R.id.fab_edit);
//        img_back_btn = (ImageView) findViewById(R.id.img_back_btn);
    }
}
