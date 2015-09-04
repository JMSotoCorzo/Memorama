package com.example.soto.memoramaprueba;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.soto.memoramaprueba.helper.UserHelper;
import com.example.soto.memoramaprueba.schema.Users;

import java.util.Map;


/**
 * Login screen.
 *
 * @author Jose Soto
 */
public class LoginActivity extends Activity {

    public static final String TAG = LoginActivity.class.getName();

    EditText mUserName;
    EditText mPassword;
    ImageView mOkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUserName = (EditText) findViewById(R.id.login_user_text);
        mPassword = (EditText) findViewById(R.id.login_password_text);
        mOkButton = (ImageView) findViewById(R.id.ok_login_button);

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map response = UserHelper.getInstance(LoginActivity.this).loginUser(mUserName.getText().toString(), mPassword.getText().toString());
                String errorProcess = (String) response.get("ERROR");
                if(errorProcess.equals(UserHelper.NO_ERROR)){
                    Intent intent = new Intent();
                    Log.e(TAG, String.valueOf(((Users) response.get("USER")).getId()));
                    intent.putExtra("userID", ((Users) response.get("USER")).getId());
                    intent.setClass(LoginActivity.this, LoggedOptionsActivity.class);

                    LoginActivity.this.startActivity(intent);
                    LoginActivity.this.finish();

                    // transition from splash to main menu
                    overridePendingTransition(R.animator.splashfadein,
                            R.animator.splashfadeout);
                }else{
                    Toast.makeText(LoginActivity.this, errorProcess, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setClass(this, WelcomeActivity.class);
        this.startActivity(intent);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserHelper.getInstance(this).release();
    }
}
