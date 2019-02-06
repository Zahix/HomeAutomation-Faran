package com.example.zahid.homeautomation;

import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class RateAppActivity extends AppCompatActivity {

    TextView titleRate, resultRate;
    Button btnFeedBack;
    ImageView charPlace, icSprite;
    RatingBar rateStars;
    Typeface MRegular, MMedium;
    String answerValue;
    Animation charAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_app);

        iniUiComponents();
        charPlace.startAnimation(charAnim);

        rateStars.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                answerValue = String.valueOf((int) (rateStars.getRating()));
                switch (answerValue) {
                    case "1":
                        charPlace.setImageResource(R.drawable.iconestar);
                        charPlace.startAnimation(charAnim);
                        resultRate.setText("Just So So");
                        break;
                    case "2":
                        charPlace.setImageResource(R.drawable.iconestar);
                        charPlace.startAnimation(charAnim);
                        resultRate.setText("Just So So");
                        break;
                    case "3":
                        charPlace.setImageResource(R.drawable.icthreestar);
                        charPlace.startAnimation(charAnim);
                        resultRate.setText("Good Job");
                        break;
                    case "4":
                        charPlace.setImageResource(R.drawable.icthreestar);
                        charPlace.startAnimation(charAnim);
                        resultRate.setText("Good Job");
                        break;
                    case "5":
                        charPlace.setImageResource(R.drawable.icfivestar);
                        charPlace.startAnimation(charAnim);
                        resultRate.setText("Awsome");
                        break;
                    default:
                        Toast.makeText(RateAppActivity.this, "No Point", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }

    private void iniUiComponents() {
        titleRate = findViewById(R.id.tv_titlerate);
        resultRate = findViewById(R.id.tv_resultrate);
        btnFeedBack = findViewById(R.id.btn_feedback);
        charPlace = findViewById(R.id.iv_charPlace);
        icSprite = findViewById(R.id.iv_icSprit);
        rateStars = findViewById(R.id.rb_ratestars);
        //importing font
        MRegular = Typeface.createFromAsset(getAssets(), "fonts/MR.ttf");
        MMedium = Typeface.createFromAsset(getAssets(), "fonts/MM.ttf");
        //customize font
        titleRate.setTypeface(MRegular);
        resultRate.setTypeface(MMedium);
        btnFeedBack.setTypeface(MMedium);
        //animation
        charAnim = AnimationUtils.loadAnimation(this,R.anim.char_anim);

    }
}
