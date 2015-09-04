package com.example.soto.memoramaprueba;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.soto.memoramaprueba.helper.UserHelper;
import com.example.soto.memoramaprueba.schema.Users;

import java.util.Map;

/**
 * Screen to create users.
 *
 * @author Jose Soto
 */
public class CreateUserActivity extends Activity {

    EditText mUserName;
    EditText mPassword;
    EditText mConfirmation;
    ImageView mOkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        mUserName = (EditText) findViewById(R.id.user_text);
        mPassword = (EditText) findViewById(R.id.new_password_text);
        mConfirmation = (EditText) findViewById(R.id.confirm_password_text);
        mOkButton = (ImageView) findViewById(R.id.ok_create_user_button);

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map response = UserHelper.getInstance(CreateUserActivity.this).createUser(mUserName.getText().toString(), mPassword.getText().toString(), mConfirmation.getText().toString());
                String errorProcess = (String) response.get("ERROR");
                if(errorProcess.equals(UserHelper.NO_ERROR)){
                    Intent intent = new Intent();
                    intent.putExtra("userID", ((Users) response.get("USER")).getId());
                    intent.setClass(CreateUserActivity.this, LoggedOptionsActivity.class);

                    CreateUserActivity.this.startActivity(intent);

                    // transition from splash to main menu
                    overridePendingTransition(R.animator.splashfadein,
                            R.animator.splashfadeout);
                }else{
                    Toast.makeText(CreateUserActivity.this, errorProcess, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setClass(this, WelcomeActivity.class);
        this.startActivity(intent);

        // transition from splash to main menu
        overridePendingTransition(R.animator.splashfadein,
                R.animator.splashfadeout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserHelper.getInstance(this).release();
    }
}
