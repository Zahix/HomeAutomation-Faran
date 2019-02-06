package com.example.zahid.homeautomation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.example.zahid.homeautomation.Utill.Common.CardTheme;
import static com.example.zahid.homeautomation.Utill.Common.STR_Account;
import static com.example.zahid.homeautomation.Utill.Common.settingStatusOnline;

public class UnitCardSettingActivity extends AppCompatActivity {

    LinearLayout ll_blue, ll_black, ll_brown, ll_white, ll_selected, ll_blue_gradient, ll_foreverlost_gradient, ll_lostmemory_gradient, ll_influenza_gradient;
    Button btn_cancel, btn_save;
    SwitchCompat sc_state;
    TextView tv_status;
    boolean statusChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_card_setting);

        initUi();
        headerComponents();
        sharedPrefranceValue();
        themeButtons();

        sc_state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                statusChanged = true;
                if (isChecked) {
                    tv_status.setText("Online");
                    settingStatusOnline = true;
                } else {
                    tv_status.setText("Offline");
                    settingStatusOnline = false;
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("CardSetting", MODE_PRIVATE).edit();
                editor.putString("cardStatus", tv_status.getText().toString());
                editor.putString("cardTheme", CardTheme);
                editor.apply();
                Intent intent = new Intent(UnitCardSettingActivity.this, IndexActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
    }

    private void themeButtons() {
        ll_white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_selected.setBackground(getResources().getDrawable(R.color.White));
                CardTheme = "White";
            }
        });
        ll_brown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_selected.setBackground(getResources().getDrawable(R.color.brown_600));
                CardTheme = "Brown";
            }
        });

        ll_blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_selected.setBackground(getResources().getDrawable(R.color.blue_900));
                CardTheme = "Blue";
            }
        });

        ll_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_selected.setBackground(getResources().getDrawable(R.color.Black));
                CardTheme = "Black";
            }
        });
        ll_blue_gradient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_selected.setBackground(getResources().getDrawable(R.drawable.bg_gradient));
                CardTheme = "BlueG";
            }
        });

        ll_foreverlost_gradient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_selected.setBackground(getResources().getDrawable(R.drawable.bg_foreverlost));
                CardTheme = "ForeverLostG";
            }
        });
        ll_lostmemory_gradient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_selected.setBackground(getResources().getDrawable(R.drawable.bg_lostmemory));
                CardTheme = "LostmemoryG";
            }
        });

        ll_influenza_gradient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_selected.setBackground(getResources().getDrawable(R.drawable.bg_influenza));
                CardTheme = "InfluenzaG";
            }
        });
    }


    private void sharedPrefranceValue() {

        SharedPreferences prefs = getSharedPreferences("CardSetting", MODE_PRIVATE);
        String restoredText = prefs.getString("cardStatus", null);
        String cardTheme = prefs.getString("cardTheme", null);
        if (cardTheme != null) {
            if (cardTheme.equals("Black")) {
                ll_selected.setBackground(getResources().getDrawable(R.color.Black));
            } else if (cardTheme.equals("White")) {
                ll_selected.setBackground(getResources().getDrawable(R.drawable.border_only));
            } else if (cardTheme.equals("Brown")) {
                ll_selected.setBackground(getResources().getDrawable(R.color.brown_600));
            } else if (cardTheme.equals("Blue")) {
                ll_selected.setBackground(getResources().getDrawable(R.color.blue_900));
            }
            else if (cardTheme.equals("BlueG")) {
                ll_selected.setBackground(getResources().getDrawable(R.drawable.bg_gradient));
            }
            else if (cardTheme.equals("ForeverLostG")) {
                ll_selected.setBackground(getResources().getDrawable(R.drawable.bg_foreverlost));
            }
            else if (cardTheme.equals("LostmemoryG")) {
                ll_selected.setBackground(getResources().getDrawable(R.drawable.bg_lostmemory));
            }
            else if (cardTheme.equals("InfluenzaG")) {
                ll_selected.setBackground(getResources().getDrawable(R.drawable.bg_influenza));
            }
        }
        if (restoredText != null) {
            if (prefs.getString("cardStatus", "Offline").equals("Online")) {
                tv_status.setText("Online");
                sc_state.setChecked(true);
            } else {
                tv_status.setText("Offline");
                sc_state.setChecked(false);
            }
        }
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

//                SharedPreferences.Editor editor = getSharedPreferences("CardSetting", MODE_PRIVATE).edit();
//                editor.putString("cardStatus", tv_status.getText().toString());
//                editor.putString("cardTheme", CardTheme);
//                editor.putInt("idName", 12);
//                editor.apply();
                onBackPressed();
//                Intent intent = new Intent(UnitCardSettingActivity.this, IndexActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                finish();
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

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(UnitCardSettingActivity.this, FirstPageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                sweetAlertDialog.cancel();
                startActivity(intent);
                finish();
            }
        });
        sweetAlertDialog.show();
    }


    private void initUi() {
        ll_black = findViewById(R.id.ll_black);
        ll_blue = findViewById(R.id.ll_blue);
        ll_brown = findViewById(R.id.ll_brown);
        ll_white = findViewById(R.id.ll_white);
        ll_blue_gradient = findViewById(R.id.ll_blue_gradient);
        ll_foreverlost_gradient = findViewById(R.id.ll_foreverlost_gradient);
        ll_lostmemory_gradient = findViewById(R.id.ll_lostmemory_gradient);
        ll_influenza_gradient = findViewById(R.id.ll_influenza_gradient);
        ll_selected = findViewById(R.id.ll_selected_theme);
        tv_status = findViewById(R.id.tv_status);
        sc_state = findViewById(R.id.sc_state);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_save = findViewById(R.id.btn_save);
    }


}
