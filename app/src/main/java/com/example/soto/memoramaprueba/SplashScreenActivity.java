package com.example.soto.memoramaprueba;

/**
 * Splash screen.
 *
 * @author Jose Soto
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class SplashScreenActivity extends Activity {

    //Set the duration of the splash screen
    private static final long SPLASH_DISPLAY_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.splash_screen_layout);

        new Handler().postDelayed(new Runnable() {
            public void run() {

                Intent intent = new Intent();
                intent.setClass(SplashScreenActivity.this, WelcomeActivity.class);

                SplashScreenActivity.this.startActivity(intent);
                SplashScreenActivity.this.finish();

                //Transition from splash to main menu
                overridePendingTransition(R.animator.splashfadein,
                        R.animator.splashfadeout);

            }
        }, SPLASH_DISPLAY_TIME);
    }

}
