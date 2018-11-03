package com.example.zahid.homeautomation;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.zahid.homeautomation.Utill.Common;
import com.example.zahid.homeautomation.Utill.EasingAnim;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText email, password;
    LinearLayout ll_login;
    ProgressBar pb;
    private boolean userVerfication = false;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


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
        if (!Validation()) {
            return;
        }
        pb.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (checkEmailVerfication()) {
                    if (task.isSuccessful()) {
                        pb.setVisibility(View.GONE);
                        Intent intent = new Intent(LogInActivity.this, MainMenuActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        pb.setVisibility(View.GONE);
                    }
                }
                else {
                    pb.setVisibility(View.GONE);
                }
            }
        });
    }

    private boolean checkEmailVerfication() {
        user = mAuth.getCurrentUser();
        if (user != null && !user.isEmailVerified()) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(LogInActivity.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();
                }
            });
            return false;
        } else {
            return true;
        }
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
