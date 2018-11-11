package com.example.zahid.homeautomation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.zahid.homeautomation.Model.Account;
import com.example.zahid.homeautomation.Model.Month;
import com.example.zahid.homeautomation.Service.BillCalculationService;
import com.example.zahid.homeautomation.Utill.Common;
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
                    if (account != null) {
                        deviceMac = account.getDevicemac();
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
                if (!deviceMac.isEmpty()) {
                    fetchMonthData();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(IndexActivity.this, "Fetch name cancelled", Toast.LENGTH_SHORT).show();
                ltv_bulb.setVisibility(View.GONE);
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

        if (id == R.id.nav_home) {
            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_profile) {
            Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_invite) {
            Toast.makeText(this, "Invite", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_changepassword) {
            Toast.makeText(this, "Change Password", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_terms) {
            Toast.makeText(this, "Terms and condition", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_faqs) {
            Toast.makeText(this, "FAQs", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_feedback) {
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
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Logout")
                .setContentText("Are you sure you want to Logout?")
                .setConfirmText("Yes")
                .setCancelText("No")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        mAuth.getInstance().signOut();
                        Intent intent = new Intent(IndexActivity.this, FirstPageActivity.class);
                        sweetAlertDialog.cancel();
                        startActivity(intent);
                        finish();
                    }
                })
                .show();
    }

    //predfined code
}
