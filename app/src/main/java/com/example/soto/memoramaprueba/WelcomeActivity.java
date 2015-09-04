package com.example.soto.memoramaprueba;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * Welcome screen.
 *
 * @author Jose Soto
 */
public class WelcomeActivity extends Activity {

    private ImageView mProfileButton;
    private ImageView mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mProfileButton = (ImageView) findViewById(R.id.profile_button);
        mLoginButton = (ImageView) findViewById(R.id.login_button);

        mProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(WelcomeActivity.this, CreateUserActivity.class);

                WelcomeActivity.this.startActivity(intent);

            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(WelcomeActivity.this, LoginActivity.class);

                WelcomeActivity.this.startActivity(intent);

            }
        });
    }

        @Override
        public void onBackPressed() {

        }



}
