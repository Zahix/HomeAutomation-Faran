package com.example.zahid.homeautomation;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.zahid.homeautomation.Utill.Common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirstPageActivity extends AppCompatActivity {
    //    KenBurnsView kenBurnsView;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        mAuth = FirebaseAuth.getInstance();
//        kenBurnsView = (KenBurnsView) findViewById(R.id.image);

//        bgImageTransition();
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        changeStatusBarColor();

    }


//    private void bgImageTransition() {
//        LinearInterpolator linearInterpolator = new LinearInterpolator();
//        AccelerateDecelerateInterpolator ACCELERATE_DECELERATE = new AccelerateDecelerateInterpolator();
//        RandomTransitionGenerator generator = new RandomTransitionGenerator(10000, linearInterpolator);
//        //duration = 10000ms = 10s and interpolator = ACCELERATE_DECELERATE
//        kenBurnsView.setTransitionGenerator(generator); //set new transition on kbv
//    }

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public void SignUpNav(View view) {
        if (!Common.From_Main_Page) {
            Common.From_Main_Page = true;
        }
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);

    }

    public void SignInNav(View view) {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        
        if (user != null && user.isEmailVerified()) {
            startActivity(new Intent(this, MainMenuActivity.class));
            finish();
        }
    }
}
