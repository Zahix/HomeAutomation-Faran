package com.example.zahid.homeautomation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.zahid.homeautomation.Model.Account;
import com.example.zahid.homeautomation.Model.Month;
import com.example.zahid.homeautomation.Service.BillCalculationService;
import com.example.zahid.homeautomation.Utill.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

    TextView tv_welcome, tv_units, tv_currency, tv_amp, tv_volts, tv_crt_Date, tv_month, tv_navbar_email, tv_navbar_name;
    FirebaseAuth mAuth;
    Query query;
    CircularImageView circleImageView, navCircleImage;
    String deviceMac, email;
    Calendar calendar;
    LottieAnimationView ltv_bulb;
    BillCalculationService billCalculationService;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    BaseActivity baseActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        Common.profile = new Account();

        baseActivity = new BaseActivity();
        billCalculationService = new BillCalculationService();

        //        if (billCalculationService.validateRequest("243")) {
//            billCalculationService.execute("243");
//            billCalculationService.execute("481");
//            billCalculationService.execute("60");
//            billCalculationService.execute("330");
//            billCalculationService.execute("460");
//            billCalculationService.execute("713");
//        }
        iniUiComponents();
        calendar = Calendar.getInstance();
        if (mAuth.getCurrentUser() != null) {
            email = mAuth.getCurrentUser().getEmail();
            fetchNameAndGender(email);
        }

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("HomeAutomation");
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        tv_navbar_email = (TextView) headerView.findViewById(R.id.tv_navbar_email);
        tv_navbar_name = (TextView) headerView.findViewById(R.id.tv_navbar_name);
        navCircleImage = (CircularImageView) headerView.findViewById(R.id.civ_navbar_avatar);

        //make translucent statusBar on kitkat devices
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

    }

    private void fetchMonthData() {

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
                    if (month != null && month.getMonth().equals("nov")) {
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(IndexActivity.this, "Database Error: " + databaseError, Toast.LENGTH_SHORT).show();
                ltv_bulb.setVisibility(View.GONE);
            }
        });
    }


    private void fetchNameAndGender(String email) {
        ltv_bulb.setVisibility(View.VISIBLE);
        if (email != null) {
            query = FirebaseDatabase.getInstance().getReference(Common.STR_Account)
                    .orderByChild("email")
                    .equalTo(email);
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
                ltv_bulb.setVisibility(View.GONE);
                if (deviceMac != null) {
                    if (!deviceMac.isEmpty()) {
                        fetchMonthData();
                    }
                } else {
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
        final CircularImageView civ_dialog_avatar = (CircularImageView) findViewById(R.id.civ_dialog_avatar);

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
        Account user = new Account(
                Common.profile.getEmail(),
                deviceMac,
                "false",
                Common.profile.getName(),
                Common.profile.getGender()
        );
        FirebaseDatabase.getInstance().getReference("account")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
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

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(IndexActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_invite) {
            Toast.makeText(this, "Invite", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_changepassword) {
            Toast.makeText(this, "Change Password", Toast.LENGTH_SHORT).show();
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
        else if (id == R.id.nav_feedback) {
            Toast.makeText(this, "Feedback", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_contact) {
            Toast.makeText(this, "Contact", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_logout) {
            if (mAuth.getCurrentUser() != null) {
                logoutDialog();
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
