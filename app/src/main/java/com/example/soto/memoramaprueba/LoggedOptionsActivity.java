package com.example.soto.memoramaprueba;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.soto.memoramaprueba.helper.UserHelper;


/**
 * Logged options screen.
 *
 * @author Jose Soto
 */
public class LoggedOptionsActivity extends Activity {


    ImageView mStartGame;
    ImageView mDeleteUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loged_options);
        mStartGame = (ImageView) findViewById(R.id.start_game);
        mDeleteUser = (ImageView) findViewById(R.id.delete_user);

        mStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoggedOptionsActivity.this, MemoramaActivity.class);
                intent.putExtra("userID", getIntent().getExtras().getLong("userID"));
                LoggedOptionsActivity.this.startActivity(intent);


            }
        });

        mDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        LoggedOptionsActivity.this);
                // set title
                alertDialogBuilder.setTitle("Delete user account?");
                alertDialogBuilder.setIcon(R.drawable.warning);

                // set dialog message
                alertDialogBuilder
                        .setMessage("You will lose your best score!")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                UserHelper.getInstance(LoggedOptionsActivity.this).deleteUser(getIntent().getExtras().getLong("userID"));
                                Intent intent = new Intent();
                                intent.setClass(LoggedOptionsActivity.this, WelcomeActivity.class);

                                LoggedOptionsActivity.this.startActivity(intent);
                                LoggedOptionsActivity.this.finish();

                                // transition from splash to main menu
                                overridePendingTransition(R.animator.splashfadein,
                                        R.animator.splashfadeout);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
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
