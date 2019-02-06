package com.example.zahid.homeautomation;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zahid.homeautomation.Model.Account;
import com.example.zahid.homeautomation.Utill.Common;
import com.example.zahid.homeautomation.Utill.Tools;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


import com.mikhaellopez.circularimageview.CircularImageView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;


import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.example.zahid.homeautomation.Utill.Common.month;

public class ProfileEditActivity extends AppCompatActivity {

    CircularImageView avatar;
    FirebaseAuth mAuth;
    TextInputEditText txtFName, txtLName, txtEmail, txtOccupation, et_address, et_city;
    Button dob_pickdate, btnUpdate;
    AppCompatSpinner sp_gender;
    private List<String> gender;
    private String selectedGender;
    BaseActivity baseActivity;
    long date_ship_millis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        baseActivity = new BaseActivity();
        mAuth = FirebaseAuth.getInstance();
        iniUiComponents();
        headerComponents();
        settingUiComponentData();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                baseActivity.warningDialog(ProfileEditActivity.this,"Sure want to update your record?","Data will be updated");
                updateDialog();
            }
        });
        dob_pickdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDatePickerLight((Button) v);
            }
        });

    }

    private void updateDialog() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setCanceledOnTouchOutside(false);
        sweetAlertDialog.setTitleText("Update");
        sweetAlertDialog.setContentText("Are you sure you want to update?");
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
                updateRecord();
                sweetAlertDialog.cancel();
                finish();
            }
        });
        sweetAlertDialog.show();

    }

    private void updateRecord() {
        final String saveGender;
        if (selectedGender == null) {
            saveGender = Common.profile.getGender();
        } else {
            saveGender = selectedGender;
        }
        Account myUser = new Account(
                txtFName.getText().toString().trim(),
                txtEmail.getText().toString().trim(),
                Common.profile.getDevicemac(),
                "false",
                saveGender,
                txtLName.getText().toString().trim(),
                dob_pickdate.getText().toString().trim(),
                txtOccupation.getText().toString().trim(),
                et_address.getText().toString().trim(),
                et_city.getText().toString().trim()
        );
//        final SweetAlertDialog sweetAlertDialog = baseActivity.progressDialog(ProfileEditActivity.this,"Updating","Request is processing...");
        FirebaseDatabase.getInstance().getReference("account")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(myUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
//                sweetAlertDialog.cancel();
                if (task.isComplete()) {
//                    Common.profile.setName(txtFName.getText().toString().trim());
//                    Common.profile.setLname(txtLName.getText().toString().trim());
//                    Common.profile.setAddress(et_address.getText().toString().trim());
//                    Common.profile.setCity(et_city.getText().toString().trim());
//                    Common.profile.setOccupation(txtOccupation.getText().toString().trim());
//                    Common.profile.setDob(dob_pickdate.getText().toString().trim());
//                    Common.profile.setGender(saveGender);
//                    Common.profile.setLiverequest("false");
//                    Common.profile.setEmail(txtEmail.getText().toString().trim());
                    Toast.makeText(getApplicationContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProfileEditActivity.this, IndexActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }

    private void iniUiComponents() {
        sp_gender = (AppCompatSpinner) findViewById(R.id.spinnerGender);
        txtFName = (TextInputEditText) findViewById(R.id.txtFName);
        txtLName = (TextInputEditText) findViewById(R.id.txtLName);
        txtEmail = (TextInputEditText) findViewById(R.id.txtEmail);
        txtOccupation = (TextInputEditText) findViewById(R.id.txtOccupation);
        et_address = (TextInputEditText) findViewById(R.id.et_address);
        et_city = (TextInputEditText) findViewById(R.id.et_city);
        dob_pickdate = (Button) findViewById(R.id.btnDOB);
        avatar = (CircularImageView) findViewById(R.id.civ_profile_avatar);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        gender = new ArrayList<>();
        gender.add("Male");
        gender.add("Female");
        gender.add("Other");


    }

    private void settingUiComponentData() {
        if (Common.profile.getGender().equals("Male")) {
            avatar.setImageResource(R.drawable.face);
        } else {
            avatar.setImageResource(R.drawable.girl);
        }
        txtFName.setText(Common.profile.getName());
        txtLName.setText(Common.profile.getLname());
        txtEmail.setText(Common.profile.getEmail());
        txtOccupation.setText(Common.profile.getOccupation());
        et_address.setText(Common.profile.getAddress());
        et_city.setText(Common.profile.getCity());
        setSpinnerData(Common.profile.getGender());
        if (!TextUtils.isEmpty(Common.profile.getDob())) {
            dob_pickdate.setText(Common.profile.getDob());
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
                Intent intent = new Intent(ProfileEditActivity.this, IndexActivity.class);
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
                Intent intent = new Intent(ProfileEditActivity.this, FirstPageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                sweetAlertDialog.cancel();
                startActivity(intent);
                finish();
            }
        });
        sweetAlertDialog.show();

    }


    private void setSpinnerData(String recentGender) {
        int sPosition = 0;
        if (recentGender.equals("Male")) {
            sPosition = 0;
        }
        if (recentGender.equals("Female")) {
            sPosition = 1;
        }
        if (recentGender.equals("Other")) {
            sPosition = 2;
        }
        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(ProfileEditActivity.this,
                android.R.layout.simple_list_item_1, gender);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_gender.setAdapter(adp1);
        sp_gender.setSelection(sPosition, true);
        sp_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long id) {

                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                selectedGender = String.valueOf(((TextView) parent.getChildAt(0)).getText());
//                Log.i("uid", String.valueOf(userid.get(position)));
//                selectedUserId = String.valueOf(userid.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void dialogDatePickerLight(final Button bt) {
        Calendar cur_calender = Calendar.getInstance();

        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        date_ship_millis = calendar.getTimeInMillis();


//                        Toast.makeText(ProfileEditActivity.this, Tools.getFormattedDateSimple(date_ship_millis), Toast.LENGTH_SHORT).show();
                        String date = DateFormat.format("dd-MM-yyyy", calendar).toString();
                        dob_pickdate.setText(date);

                    }

                },
                cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH)
        );
        //set dark light
        datePicker.setThemeDark(true);

        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
//        datePicker.setMinDate(cur_calender);
        datePicker.setMaxDate(cur_calender);
        datePicker.show(getFragmentManager(), "Datepickerdialog");
    }
}
