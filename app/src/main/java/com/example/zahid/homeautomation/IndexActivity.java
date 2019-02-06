package com.example.zahid.homeautomation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.zahid.homeautomation.Model.Account;
import com.example.zahid.homeautomation.Model.Month;
import com.example.zahid.homeautomation.Model.UnitCalOffline;
import com.example.zahid.homeautomation.Service.BillCalculationService;
import com.example.zahid.homeautomation.Utill.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;


import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class IndexActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    LinearLayout ll_box1, ll_box2, ll_box3, ll_box4, ll_header;
    ImageView iv_setting, iv_thunder_icon, iv_thunder_icon4, iv_thunder_mainIcon;
    TextView tv_welcome, tv_units, tv_currency, tv_amp, tv_volts, tv_crt_Date, tv_month, tv_navbar_email, tv_navbar_name, tv_b1, tv_b2, tv_b3, tv_b4;
    RelativeLayout rv_main_card;
    FirebaseAuth mAuth;
    Query query;
    CircularImageView circleImageView, navCircleImage;
    String deviceMac, email;
    Calendar calendar;
    LottieAnimationView ltv_bulb;
    BillCalculationService billCalculationService;
    //    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    BaseActivity baseActivity;
    String cardTheme = "White", cardStatus = "Online";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        //getting theme setting and or so
        sharedPrefranceValue();

        Common.profile = new Account();
        baseActivity = new BaseActivity();
        billCalculationService = new BillCalculationService();
        iniUiComponents();
        calendar = Calendar.getInstance();
        if (mAuth.getCurrentUser() != null) {
            email = mAuth.getCurrentUser().getEmail();
            fetchNameAndGender(email);
        }

        View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        tv_navbar_email = (TextView) headerView.findViewById(R.id.tv_navbar_email);
        tv_navbar_name = (TextView) headerView.findViewById(R.id.tv_navbar_name);
        navCircleImage = (CircularImageView) headerView.findViewById(R.id.civ_navbar_avatar);

        //make translucent statusBar on kitkat devices
//        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
//            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
//        }
//        if (Build.VERSION.SDK_INT >= 19) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        }
//        //make fully Android Transparent Status bar
//        if (Build.VERSION.SDK_INT >= 21) {
//            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            if (getSupportActionBar() != null) {
//                getSupportActionBar().hide();
//            }
//        }
//        getSupportActionBar().hide();
        headerComponents();

        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                addUnitsOfflineDialog();
                Intent intent = new Intent(IndexActivity.this, UnitCardSettingActivity.class);
                startActivity(intent);
            }
        });
        iv_thunder_mainIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation rotation = AnimationUtils.loadAnimation(IndexActivity.this, R.anim.zoom_in);
                rotation.setRepeatCount(Animation.INFINITE);
                iv_thunder_mainIcon.startAnimation(rotation);
                addUnitsOfflineDialog();

            }
        });

    }

    private void addUnitsOfflineDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.add_units_offline);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final TextView email = (TextView) dialog.findViewById(R.id.tv_email);
        final TextView userName = (TextView) dialog.findViewById(R.id.tv_name);
        final EditText et_units = (EditText) dialog.findViewById(R.id.et_units);
        final EditText et_ampere = (EditText) dialog.findViewById(R.id.et_ampere);
        final EditText et_volts = (EditText) dialog.findViewById(R.id.et_volts);

        final CircularImageView civ_dialog_avatar = (CircularImageView) dialog.findViewById(R.id.civ_dialog_avatar);

        email.setText(Common.profile.getEmail());
        userName.setText(Common.profile.getName());
        if (Common.profile == null || Common.profile.getEmail() == null || Common.profile.getEmail().equals("")) {
            return;
        }
        if (Common.profile.getGender().equals("Female") || Common.profile.getGender().equals("Other")) {
            civ_dialog_avatar.setImageResource(R.drawable.girl);
        }

        ((AppCompatButton) dialog.findViewById(R.id.bt_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((AppCompatButton) dialog.findViewById(R.id.bt_submit)).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (et_units != null) {
                    Common.unitCalService = new UnitCalOffline(
                            et_volts.getText().toString().trim(),
                            et_ampere.getText().toString().trim(),
                            et_units.getText().toString().trim()
                    );
                    if (billCalculationService.validateRequest(Common.unitCalService.getUnits())) {
                        Common.offlineRPS = String.valueOf(billCalculationService.execute(Common.unitCalService.getUnits()));

                        tv_units.setText(et_units.getText().toString().trim() + " Unit");
                        tv_amp.setText(et_ampere.getText().toString().trim() + " Amp");
                        tv_volts.setText(et_volts.getText().toString().trim() + " Volt");
                        tv_crt_Date.setText("Last Updated at: " + String.valueOf(calendar.get(Calendar.HOUR)) + ":00");
                        tv_month.setText("Offline");
                        tv_currency.setText(Common.offlineRPS + " Rs");
                    }

                } else {
                    Toast.makeText(IndexActivity.this, "Enter Units", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.dismiss();
            }

        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void sharedPrefranceValue() {
        SharedPreferences prefs = getSharedPreferences("CardSetting", MODE_PRIVATE);
        String restoredText = prefs.getString("cardStatus", null);
        if (restoredText != null) {
            cardStatus = prefs.getString("cardStatus", "Offline");
            cardTheme = prefs.getString("cardTheme", "White");
        }
    }

    private void headerComponents() {
        View backPressed = findViewById(R.id.back_btn);
        backPressed.setVisibility(View.GONE);
        View home = findViewById(R.id.imageviewHome);
        home.setVisibility(View.GONE);
        ll_header = findViewById(R.id.ll_head);
        ll_header.setVisibility(View.INVISIBLE);
        View navBtn = findViewById(R.id.iv_navbar);
        navBtn.setVisibility(View.VISIBLE);
        navBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.START);
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

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void iniUiComponents() {
        iv_thunder_mainIcon = (ImageView) findViewById(R.id.iv_thunder_mainIcon);
        iv_thunder_icon4 = (ImageView) findViewById(R.id.iv_thunder_icon4);
        iv_thunder_icon = (ImageView) findViewById(R.id.iv_thunder_icon);
        iv_setting = (ImageView) findViewById(R.id.iv_setting);
        tv_month = (TextView) findViewById(R.id.tv_month);
        ltv_bulb = (LottieAnimationView) findViewById(R.id.lav_loading);
        tv_units = (TextView) findViewById(R.id.tv_units);
        tv_currency = (TextView) findViewById(R.id.tv_currency);
        tv_amp = (TextView) findViewById(R.id.tv_amp);
        tv_volts = (TextView) findViewById(R.id.tv_volts);
        mAuth = FirebaseAuth.getInstance();
        tv_welcome = (TextView) findViewById(R.id.tv_welcome);
        circleImageView = (CircularImageView) findViewById(R.id.civ_profileImage);
        tv_crt_Date = (TextView) findViewById(R.id.tv_crt_date);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        rv_main_card = (RelativeLayout) findViewById(R.id.rv_main_card);
        ll_box1 = findViewById(R.id.ll_box1);
        ll_box2 = findViewById(R.id.ll_box2);
        ll_box3 = findViewById(R.id.ll_box3);
        ll_box4 = findViewById(R.id.ll_box4);
        tv_b1 = findViewById(R.id.tv_b1);
        tv_b2 = findViewById(R.id.tv_b2);
        tv_b3 = findViewById(R.id.tv_b3);
        tv_b4 = findViewById(R.id.tv_b4);

    }


    private void fetchMonthData() {
        Calendar c = Calendar.getInstance();
        final String monthCode = String.valueOf(c.get(Calendar.MONTH) + 1);


        if (cardStatus.equals("Offline")) {
            return;
        }
//        if (!settingStatusOnline) {
//            return;
//        }
        ltv_bulb.setVisibility(View.VISIBLE);
        Query monthQuery = FirebaseDatabase.getInstance().getReference(Common.STR_Month)
                .orderByChild("devicemac")
                .equalTo(deviceMac);
        monthQuery.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot monthDataSnapShot : dataSnapshot.getChildren()) {
                    Month month = monthDataSnapShot.getValue(Month.class);
                    if (month != null && month.getMonthcode().equals(monthCode)) {
                        tv_units.setText(month.getMounits() + " Unit");
                        tv_amp.setText(month.getMomaxampere() + " Amp");
                        tv_volts.setText(month.getMomaxvoltage() + " Volt");
                        tv_crt_Date.setText("Last Updated at: " + String.valueOf(calendar.get(Calendar.HOUR)) + ":00");
                        tv_month.setText("Month: " + month.getMonth());
                        if (billCalculationService.validateRequest(month.getMounits())) {
                            tv_currency.setText(String.valueOf(billCalculationService.execute(month.getMounits()) + " Rs"));
                        }
                    }
                }
                ltv_bulb.setVisibility(View.GONE);
                Animation rotation = AnimationUtils.loadAnimation(IndexActivity.this, R.anim.rotation_clockwise);
                rotation.setRepeatCount(Animation.INFINITE);
                iv_thunder_icon.startAnimation(rotation);

                Animation rotation2 = AnimationUtils.loadAnimation(IndexActivity.this, R.anim.rotation_anticlockwise);
                rotation2.setRepeatCount(Animation.INFINITE);
                iv_thunder_icon4.startAnimation(rotation2);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(IndexActivity.this, "Database Error: " + databaseError, Toast.LENGTH_SHORT).show();
                ltv_bulb.setVisibility(View.GONE);
            }
        });
    }

    private void themeChanger() {
        //Theme
        Integer selectedThemeTcolor, selectedThemeDrawable, selectedThemeBcolor;
        selectedThemeTcolor = R.color.Black;
        selectedThemeDrawable = R.drawable.setting;
        selectedThemeBcolor = R.color.White;
        switch (cardTheme) {
            case "Black":
                selectedThemeTcolor = R.color.White;
                selectedThemeDrawable = R.drawable.setting_white;
                selectedThemeBcolor = R.color.Black;
                break;
            case "Brown":
                selectedThemeTcolor = R.color.White;
                selectedThemeDrawable = R.drawable.setting_white;
                selectedThemeBcolor = R.color.brown_600;
                break;
            case "Blue":
                selectedThemeTcolor = R.color.White;
                selectedThemeDrawable = R.drawable.setting_white;
                selectedThemeBcolor = R.color.blue_900;
                break;
            case "White":
                selectedThemeTcolor = R.color.Black;
                selectedThemeDrawable = R.drawable.setting;
                selectedThemeBcolor = R.color.White;
                break;

            case "BlueG":
                selectedThemeTcolor = R.color.Black;
                selectedThemeDrawable = R.drawable.setting;
                selectedThemeBcolor = R.drawable.bg_gradient;
                break;

            case "ForeverLostG":
                selectedThemeTcolor = R.color.White;
                selectedThemeDrawable = R.drawable.setting_white;
                selectedThemeBcolor = R.drawable.bg_foreverlost;
                break;

            case "LostmemoryG":
                selectedThemeTcolor = R.color.Black;
                selectedThemeDrawable = R.drawable.setting;
                selectedThemeBcolor = R.drawable.bg_lostmemory;
                break;

            case "InfluenzaG":
                selectedThemeTcolor = R.color.White;
                selectedThemeDrawable = R.drawable.setting_white;
                selectedThemeBcolor = R.drawable.bg_influenza;
                break;
        }
        if (cardTheme.equals("InfluenzaG") || cardTheme.equals("LostmemoryG") || cardTheme.equals("ForeverLostG") || cardTheme.equals("BlueG")) {
            rv_main_card.setBackground(this.getResources().getDrawable(selectedThemeBcolor));
        } else {
            rv_main_card.setBackgroundColor(this.getResources().getColor(selectedThemeBcolor));
        }
        tv_month.setTextColor(this.getResources().getColor(selectedThemeTcolor));
        tv_units.setTextColor(this.getResources().getColor(selectedThemeTcolor));
        tv_currency.setTextColor(this.getResources().getColor(selectedThemeTcolor));
        tv_amp.setTextColor(this.getResources().getColor(selectedThemeTcolor));
        tv_volts.setTextColor(this.getResources().getColor(selectedThemeTcolor));
        tv_crt_Date.setTextColor(this.getResources().getColor(selectedThemeTcolor));
        iv_setting.setImageResource(selectedThemeDrawable);
        //MiniBoxes
        if (cardTheme.equals("InfluenzaG") || cardTheme.equals("LostmemoryG") || cardTheme.equals("ForeverLostG") || cardTheme.equals("BlueG")) {
            ll_box1.setBackground(this.getResources().getDrawable(selectedThemeBcolor));
            ll_box2.setBackground(this.getResources().getDrawable(selectedThemeBcolor));
            ll_box3.setBackground(this.getResources().getDrawable(selectedThemeBcolor));
            ll_box4.setBackground(this.getResources().getDrawable(selectedThemeBcolor));
        } else {
            ll_box1.setBackgroundColor(this.getResources().getColor(selectedThemeBcolor));
            ll_box2.setBackgroundColor(this.getResources().getColor(selectedThemeBcolor));
            ll_box3.setBackgroundColor(this.getResources().getColor(selectedThemeBcolor));
            ll_box4.setBackgroundColor(this.getResources().getColor(selectedThemeBcolor));
        }
        tv_b1.setTextColor(this.getResources().getColor(selectedThemeTcolor));
        tv_b2.setTextColor(this.getResources().getColor(selectedThemeTcolor));
        tv_b3.setTextColor(this.getResources().getColor(selectedThemeTcolor));
        tv_b4.setTextColor(this.getResources().getColor(selectedThemeTcolor));

    }

    private void fetchNameAndGender(String email) {
        ltv_bulb.setVisibility(View.VISIBLE);
        if (email != null) {
            query = FirebaseDatabase.getInstance().getReference(Common.STR_Account)
                    .orderByChild("email")
                    .equalTo(email);
        } else {
            return;
        }
        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot accountDataSnapShot : dataSnapshot.getChildren()) {
                    Account account = accountDataSnapShot.getValue(Account.class);

                    Common.profile = account;

                    if (account != null) {
                        if (account.getDevicemac() != null) {
                            deviceMac = account.getDevicemac();
                        }
                        tv_welcome.setText("Welcome " + account.getName());
                        if (!account.getGender().equals("Male")) {
                            circleImageView.setImageResource(R.drawable.girl);
                            navCircleImage.setImageResource(R.drawable.girl);
                        }
                        tv_navbar_email.setText(account.getEmail());
                        tv_navbar_name.setText(account.getName());
                    }
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().show();
                    }
                }
                ll_header.setVisibility(View.VISIBLE);
                ltv_bulb.setVisibility(View.GONE);
                themeChanger();
                if (deviceMac != null) {
                    if (!deviceMac.isEmpty()) {
                        fetchMonthData();
                    }
                } else if (Common.StateForMac) {
                    addDeviceMacDialog();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(IndexActivity.this, "Fetch name cancelled", Toast.LENGTH_SHORT).show();
                ltv_bulb.setVisibility(View.GONE);
            }
        });
    }

    private void addDeviceMacDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.add_device_mac);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final TextView email = (TextView) dialog.findViewById(R.id.tv_email);
        final TextView userName = (TextView) dialog.findViewById(R.id.tv_name);
        final EditText et_post = (EditText) dialog.findViewById(R.id.et_post);
        final CircularImageView civ_dialog_avatar = dialog.findViewById(R.id.civ_dialog_avatarb);

        email.setText(Common.profile.getEmail());
        userName.setText(Common.profile.getName());
        if (Common.profile == null || Common.profile.getEmail() == null || Common.profile.getEmail().equals("")) {
            return;
        }
        if (Common.profile.getGender().equals("Female") || Common.profile.getGender().equals("Other")) {
            civ_dialog_avatar.setImageResource(R.drawable.girl);
        }

        ((AppCompatButton) dialog.findViewById(R.id.bt_offline)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Common.StateForMac = false;
            }
        });
        ((AppCompatButton) dialog.findViewById(R.id.bt_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((AppCompatButton) dialog.findViewById(R.id.bt_submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String review = et_post.getText().toString().trim();
                if (review.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter device MAC", Toast.LENGTH_SHORT).show();
                } else {
                    saveDeviceMacInDb(et_post.getText().toString().trim());
                }
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void saveDeviceMacInDb(final String deviceMac) {
        Account myUser = new Account(
                Common.profile.getName(),
                Common.profile.getEmail(),
                deviceMac,
                "false",
                Common.profile.getGender(),
                Common.profile.getLname(),
                Common.profile.getDob(),
                Common.profile.getOccupation(),
                Common.profile.getAddress(),
                Common.profile.getCity()

        );

        FirebaseDatabase.getInstance().getReference("account")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(myUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    baseActivity.sucessDialog(IndexActivity.this, "Device attached to your account.", "Device", null);
                }
            }
        });
    }

    public void accountList(View view) {
        Intent intent = new Intent(this, AccountList.class);
        startActivity(intent);
    }

    public void barChartActivity(View view) {
        Intent intent = new Intent(this, BarChartActivity.class);
        startActivity(intent);
    }

    public void lineChartActivity(View view) {
        Intent intent = new Intent(this, LineChartActivity.class);
        startActivity(intent);
    }


    public void refresh(View view) {
        if (mAuth.getCurrentUser() != null) {
            email = mAuth.getCurrentUser().getEmail();
            fetchNameAndGender(email);
        }
    }


    //Predefined code
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.index, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        item.setCheckable(false);
        if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, ProfileSettingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_changepassword) {
            showChangePasswordDialog();
        } else if (id == R.id.nav_adddevice) {
            if (Common.profile.getDevicemac() == null) {
                addDeviceMacDialog();
            } else {
                Toast.makeText(IndexActivity.this, "Device is already attached", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_terms) {
            Toast.makeText(this, "Terms and condition", Toast.LENGTH_SHORT).show();
        }
//        else if (id == R.id.nav_faqs) {
//            Toast.makeText(this, "FAQs", Toast.LENGTH_SHORT).show();
//        }
        else if (id == R.id.nav_about_us) {
            Intent intent = new Intent(IndexActivity.this, AboutUsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_feedback) {
//            Intent intent = new Intent(IndexActivity.this, CustomerFeedBackActivity.class);
            Intent intent = new Intent(IndexActivity.this, RateAppActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_contact) {
            Intent intent = new Intent(IndexActivity.this, ContactUs.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            if (mAuth.getCurrentUser() != null) {
                logoutDialog();
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showChangePasswordDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.change_password_dialog);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final EditText oldPassword = (EditText) dialog.findViewById(R.id.et_oldPassword);
        final EditText newPassword = (EditText) dialog.findViewById(R.id.et_newpassword);
        final EditText confirmPassword = (EditText) dialog.findViewById(R.id.confirmPassword);
        final CircularImageView civ_profile = (CircularImageView) dialog.findViewById(R.id.civ_dialog_avatar);
        final TextView tv_name = (TextView) dialog.findViewById(R.id.tv_name);
        final TextView tv_email = (TextView) dialog.findViewById(R.id.tv_email);

        if (!Common.profile.getGender().equals("Male")) {
            civ_profile.setImageResource(R.drawable.girl);
        }
        tv_name.setText(Common.profile.getName());
        tv_email.setText(Common.profile.getEmail());

        ((AppCompatButton) dialog.findViewById(R.id.bt_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ((AppCompatButton) dialog.findViewById(R.id.bt_submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newPassword.getText().toString().trim().equals(confirmPassword.getText().toString().trim())
                        & !newPassword.getText().toString().trim().equals("")
                        & !newPassword.getText().toString().trim().isEmpty()
                        & !confirmPassword.getText().toString().trim().isEmpty()
                        & !confirmPassword.getText().toString().trim().equals("")
                        & !oldPassword.getText().toString().trim().isEmpty()) {
                    updatePasswordInDb(newPassword.getText().toString().trim(), oldPassword.getText().toString().trim());
                    dialog.dismiss();
                } else if (oldPassword.getText().toString().trim().isEmpty()) {
                    Toast.makeText(IndexActivity.this, "Enter old password", Toast.LENGTH_SHORT).show();
                } else if (newPassword.getText().toString().trim().isEmpty()
                        || confirmPassword.getText().toString().trim().isEmpty()) {
                    Toast.makeText(IndexActivity.this, "Enter new or confirm password", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(IndexActivity.this, "Password must match confirm password", Toast.LENGTH_SHORT).show();
                    newPassword.setText("");
                    confirmPassword.setText("");
                }
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void updatePasswordInDb(final String newPassword, String oldPassword) {
        final SweetAlertDialog prgressDialog = baseActivity.progressDialog(IndexActivity.this, "Please Wait", "Processing...");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(Common.profile.getEmail(), oldPassword);
        if (user != null) {
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    prgressDialog.cancel();
                                    baseActivity.sucessDialog(IndexActivity.this, "Password change successful", "Successful", null);

                                } else {
                                    prgressDialog.cancel();
                                    baseActivity.errorDialog(IndexActivity.this, "Failed to change password", "Failed");
                                }
                            }
                        });
                    } else {
                        prgressDialog.cancel();
                        baseActivity.errorDialog(IndexActivity.this, "Old password is wrong", "Failed");
                    }
                }
            });

        }
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
                Intent intent = new Intent(IndexActivity.this, FirstPageActivity.class);
                sweetAlertDialog.cancel();
                startActivity(intent);
                finish();
            }
        });
        sweetAlertDialog.show();

    }

    //predfined code
}
