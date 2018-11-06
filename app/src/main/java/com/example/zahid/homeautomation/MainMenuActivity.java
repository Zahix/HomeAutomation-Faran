package com.example.zahid.homeautomation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.TimedText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.zahid.homeautomation.Model.Account;
import com.example.zahid.homeautomation.Model.Month;
import com.example.zahid.homeautomation.Service.BillCalculationService;
import com.example.zahid.homeautomation.Utill.Common;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.Calendar;

import javax.xml.transform.Result;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainMenuActivity extends AppCompatActivity {

    TextView tv_welcome, tv_units, tv_currency, tv_amp, tv_volts, tv_crt_Date;
    FirebaseAuth mAuth;
    Query query;
    CircleImageView circleImageView;
    String deviceMac, email;
    Calendar calendar;
    LottieAnimationView ltv_bulb;
    BillCalculationService billCalculationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        billCalculationService = new BillCalculationService();

//        if (billCalculationService.validateRequest("243")) {
//            billCalculationService.execute("243");
//            billCalculationService.execute("481");
//            billCalculationService.execute("60");
//            billCalculationService.execute("330");
//            billCalculationService.execute("460");
//            billCalculationService.execute("713");
//        }


        ltv_bulb = (LottieAnimationView) findViewById(R.id.lav_loading);
        tv_units = (TextView) findViewById(R.id.tv_units);
        tv_currency = (TextView) findViewById(R.id.tv_currency);
        tv_amp = (TextView) findViewById(R.id.tv_amp);
        tv_volts = (TextView) findViewById(R.id.tv_volts);
        mAuth = FirebaseAuth.getInstance();
        tv_welcome = (TextView) findViewById(R.id.tv_welcome);
        circleImageView = (CircleImageView) findViewById(R.id.civ_profileImage);
        tv_crt_Date = (TextView) findViewById(R.id.tv_crt_date);

        calendar = Calendar.getInstance();
        if (mAuth.getCurrentUser() != null) {
            email = mAuth.getCurrentUser().getEmail();
            fetchNameAndGender(email);
        }

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
                        tv_amp.setText(month.getMomaxampere()+" Amp");
                        tv_volts.setText(month.getMomaxvoltage()+" Volt");
                        tv_crt_Date.setText("Last Updated at: " + String.valueOf(calendar.get(Calendar.HOUR)) + ":00");

                        if (billCalculationService.validateRequest(month.getMounits())){
                            tv_currency.setText(String.valueOf(billCalculationService.execute(month.getMounits()) +" Rs"));
                        }
                    }
                }
                ltv_bulb.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainMenuActivity.this, "Database Error: " + databaseError, Toast.LENGTH_SHORT).show();
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
                        }
                    }
                }
                ltv_bulb.setVisibility(View.GONE);
                if (!deviceMac.isEmpty()) {
                    fetchMonthData();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainMenuActivity.this, "Fetch name cancelled", Toast.LENGTH_SHORT).show();
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
}
