package com.example.zahid.homeautomation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.zahid.homeautomation.Utill.Common;
import com.mikhaellopez.circularimageview.CircularImageView;

public class ProfileActivity extends AppCompatActivity {

    TextView username,email;
    CircularImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        username = (TextView) findViewById(R.id.tv_username);
        email = (TextView) findViewById(R.id.tv_profile_email);
        avatar = (CircularImageView) findViewById(R.id.civ_profile_avatar);

        if (Common.profile!=null && Common.profile.getEmail()!=null){
            username.setText(Common.profile.getName());
            email.setText(Common.profile.getEmail());

            if (Common.profile.getGender().equals("Female")|| Common.profile.getGender().equals("Other")){
                avatar.setImageResource(R.drawable.girl);
            }
        }
    }
}
