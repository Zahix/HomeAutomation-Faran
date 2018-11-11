package com.example.zahid.homeautomation;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zahid.homeautomation.Model.Account;
import com.example.zahid.homeautomation.Model.User;
import com.example.zahid.homeautomation.Utill.Common;
import com.example.zahid.homeautomation.Utill.EasingAnim;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

import android.app.Dialog;

public class LogInActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText email, password;
    LinearLayout ll_login;
    ProgressBar pb;
    FirebaseUser user;
    private boolean activityState = true;
    private boolean processing = false;
    BaseActivity baseActivity;
    Account myAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        myAccount = new Account();
        baseActivity = new BaseActivity();
        mAuth = FirebaseAuth.getInstance();
        pb = (ProgressBar) findViewById(R.id.pb);
        email = (EditText) findViewById(R.id.et_email);
        email.setPadding(0, 15, 0, 15);
        password = (EditText) findViewById(R.id.et_password);
        password.setPadding(0, 15, 0, 15);
        ll_login = (LinearLayout) findViewById(R.id.ll_button);
        ease(ll_login);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        changeStatusBarColor();


    }

    public void SignUpNav(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    public void MainMenuNav(View view) {
        userLogin();
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void ease(final View view) {
        EasingAnim easing = new EasingAnim(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        float fromY = 600;
        float toY = view.getTop();
        ValueAnimator valueAnimatorY = ValueAnimator.ofFloat(fromY, toY);

        valueAnimatorY.setEvaluator(easing);

        valueAnimatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setTranslationY((float) animation.getAnimatedValue());
            }
        });

        animatorSet.playTogether(valueAnimatorY);
        animatorSet.setDuration(700);
        animatorSet.start();
    }

    private void userLogin() {
        if (processing) {
            return;
        }

        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!baseActivity.hasInternet(connectivity)) {
//            Toast.makeText(this, "Check your internet", Toast.LENGTH_SHORT).show();
            baseActivity.warningDialog(LogInActivity.this, "Check your internet", "Internet not found");
            return;
        }
        if (!Validation()) {
            return;
        }

        pb.setVisibility(View.VISIBLE);
//        showSweetLoadingDialog();
        processing = true;
        mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                processing = false;
                if (task.isSuccessful()) {
                    pb.setVisibility(View.GONE);
                    ll_login.setEnabled(true);
                    if (checkEmailVerfication()) {
                        getUserData();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    pb.setVisibility(View.GONE);
                    ll_login.setEnabled(true);
                }

            }
        });


    }

    private void indexActivityNav() {
        Intent intent = new Intent(LogInActivity.this, IndexActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    //    private void showSweetLoadingDialog(){
//        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText("Loading");
//        pDialog.setCancelable(false);
//        pDialog.show();
//    }
    private void getUserData() {
        if (mAuth.getCurrentUser() != null) {
            FirebaseDatabase.getInstance().getReference("account")
                    .orderByChild("email")
                    .equalTo(mAuth.getCurrentUser().getEmail()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot accountDataSnapShot : dataSnapshot.getChildren()) {
                        myAccount = accountDataSnapShot.getValue(Account.class);
                    }

                    if (myAccount != null && myAccount.getDevicemac() == null) {
                        addDeviceMacDialog();
                    }
                    if (myAccount != null && myAccount.getDevicemac() != null) {
                        indexActivityNav();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(LogInActivity.this, "Failed to fetch", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean checkEmailVerfication() {
        user = mAuth.getCurrentUser();
        if (user != null && !user.isEmailVerified()) {
            verificationEmailDialogue(user);
            return false;
        } else {
            return true;
        }
    }

    private void verificationEmailDialogue(final FirebaseUser user) {
        if (activityState) {
            try {
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Account is not verified!")
                        .setContentText("Would you like us to send verification email?")
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
                                baseActivity.sendEmail(user);
                                sweetAlertDialog.cancel();
                                baseActivity.sucessDialog(LogInActivity.this, "Verification email send", "", user);
                            }
                        })
                        .show();


            } catch (Exception e) {
                Toast.makeText(this, "Verification dialog failed to load: " + e, Toast.LENGTH_SHORT).show();
                Log.e("verification", String.valueOf(e));
            }

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityState = false;
    }

    private void addDeviceMacDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.add_device_mac);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final TextView email = (TextView) dialog.findViewById(R.id.tv_email);
        final TextView userName = (TextView) dialog.findViewById(R.id.tv_name);
        final EditText et_post = (EditText) dialog.findViewById(R.id.et_post);

        email.setText(myAccount.getEmail());
        userName.setText(myAccount.getName());

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
                email.getText().toString().trim(),
                deviceMac,
                "false",
                myAccount.getName(),
                myAccount.getGender()
        );
        FirebaseDatabase.getInstance().getReference("account")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    Intent intent = new Intent(LogInActivity.this, IndexActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private boolean Validation() {
        String validation = Common.Validation("default", email.getText().toString().trim(), password.getText().toString().trim());

        if (validation.equals("required email")) {
            email.setError("Email is required");
            email.requestFocus();
            return false;
        }
        if (validation.equals("required password")) {
            password.setError("Required password");
            password.requestFocus();
            return false;
        }
        if (validation.equals("vaildate email")) {
            email.setError("Please enter a vail email");
            email.requestFocus();
            return false;
        }
        if (validation.equals("min password")) {
            password.setError("Minimum password length is 6");
            password.requestFocus();
            return false;
        }
        return true;
    }


}
