package cu.sld.farmacoshlucia;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.sld.farmacoshlucia.R;

public class Splash_Activity extends  AppCompatActivity{
    ImageView imageView;
    View mobcook;
    TextView Text;
    LottieAnimationView animation;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_splash);
        imageView = findViewById(R.id.imageView);
        mobcook = findViewById(R.id.mobcook);
        Text = findViewById(R.id.textView);
        animation = findViewById(R.id.mobcook);

        TextView textView = (TextView)findViewById(R.id.textView);
        textView.setText(Html.fromHtml(getResources().getString(R.string.tex_splash)));

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_fade);
        imageView.startAnimation(animation);

        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_fade);
        mobcook.startAnimation(animation1);

        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_fade);
        Text.startAnimation(animation2);

        //this.getSupportActionBar().hide();
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // add animation of text_logo
        Animation am = new AlphaAnimation(0.0f, 1.0f);
        am.setDuration(2000);
        am.setRepeatCount(0);
        Text.startAnimation(am);

        imageView.animate().translationY(-2000).setDuration(1000).setStartDelay(4000);
        mobcook.animate().translationY(2000).setDuration(1000).setStartDelay(4000);
        Text.animate().translationY(-2000).setDuration(1000).setStartDelay(4000);









        Thread td = new Thread(){
            public void run(){
                try {
                    sleep(5000);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                finally {

                    Intent intent = new Intent(Splash_Activity.this , MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };td.start();
    }



}