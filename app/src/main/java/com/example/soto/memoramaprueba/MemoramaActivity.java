package com.example.soto.memoramaprueba;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.soto.memoramaprueba.helper.UserHelper;
import com.example.soto.memoramaprueba.models.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;


/**
 * Game screen. The purpose of the game is find pairs of images in the less time.
 *
 * @author Jose Soto
 */
public class MemoramaActivity extends Activity{

    private static final int YOU_LOSE = 0;
    private static final int YOU_WIN = 1;
    private static final int BEST_SCORE = 2;

    private static final String TAG = MemoramaActivity.class.getName();
    private static final String CARDS = "Cards";
    private static final String VISIBLE_CARD = "Visible card";
    private static final String DISABLED_CARDS = "Disabled cards";
    private static final String TIME = "Time";

    private static final int TABLE_SIZE = 4;

    public static final int[] FLAG_RESOURCES = new int[]{
            R.drawable.ao,
            R.drawable.ar,
            R.drawable.as,
            R.drawable.at,
            R.drawable.au,
            R.drawable.aw,
            R.drawable.ax,
            R.drawable.ba,
            R.drawable.bb,
            R.drawable.bd,
            R.drawable.be,
            R.drawable.bf,
            R.drawable.bg
    };

    private Card[] cards;
    private Card visibleCard = null;
    private Handler mHandler = new Handler();
    private TableLayout tableCard;
    private TextView timer;
    private ArrayList<Integer> disabledCards;

    private Bundle recoveryInstance = null;
    private boolean restored = false;

    private long startTime = 0L;
    private Handler timeHandler = new Handler();
    private long timeInMilliseconds = 0L;

    private int gameStatus;
    private boolean gameOver = false;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //Always call superclass first
        setContentView(R.layout.memorama_layout);

        if(savedInstanceState != null){
            // Restore value of members from saved state
            cards = (Card[]) savedInstanceState.getParcelableArray(CARDS);
            visibleCard = savedInstanceState.getParcelable(VISIBLE_CARD);
            disabledCards = savedInstanceState.getIntegerArrayList(DISABLED_CARDS);
        }else{
            cards = createCells(TABLE_SIZE * TABLE_SIZE);
            visibleCard = null;
            disabledCards = new ArrayList<>();
            startTime = SystemClock.uptimeMillis();
        }

        tableCard = (TableLayout) findViewById(R.id.cardGrid);
        for (int y = 0; y < TABLE_SIZE; y++) {
            final TableRow row = new TableRow(this);
            for (int x = 0; x < TABLE_SIZE; x++) {
                row.addView(cards[(y*TABLE_SIZE) + x].getViewAnimator());
            }

            tableCard.addView(row);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        timer = new TextView(this);
        timer.setText(getString(R.string.timerVal));
        timer.setPadding(5, 5, 5, 5);
        timer.setTypeface(null, Typeface.BOLD);
        timer.setTextSize(22);
        menu.add(0, 1, 1, R.string.timerVal).setActionView(timer).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        timeHandler.postDelayed(updateTimerThread, 0);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.e(TAG, "onSaveInstanceState ENTERS");
        //Remove the parent from every viewAnimator in the cards
        for (Card item : cards) {
            if(item.getViewAnimator() != null && item.getViewAnimator().getParent() != null){
                ((ViewGroup) item.getViewAnimator().getParent()).removeView(item.getViewAnimator());
            }

        }
        // Save the user's current game state
        outState.putParcelableArray(CARDS, cards);
        if(visibleCard != null){
            outState.putParcelable(VISIBLE_CARD, visibleCard);
        }
        outState.putIntegerArrayList(DISABLED_CARDS, disabledCards);
        outState.putLong(TIME, startTime);
        recoveryInstance = outState;
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.e(TAG, "onRestoreInstanceState ENTERS");

        if(savedInstanceState != null){
            //Restore value of members from saved state
            cards = (Card[]) savedInstanceState.getParcelableArray(CARDS);
            for (Card item : cards) {
                item.init(this);
            }
            visibleCard = savedInstanceState.getParcelable(VISIBLE_CARD);
            if (visibleCard != null) {
                visibleCard.init(this);
            }
            disabledCards = savedInstanceState.getIntegerArrayList(DISABLED_CARDS);

            tableCard.removeAllViews();
            for (int y = 0; y < TABLE_SIZE; y++) {
                final TableRow row = new TableRow(this);
                for (int x = 0; x < TABLE_SIZE; x++) {
                    row.addView(cards[(y*TABLE_SIZE) + x].getViewAnimator());
                }

                tableCard.addView(row);
            }

            startTime = savedInstanceState.getLong(TIME);
            restored = true;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(recoveryInstance != null && !restored){
            Log.e(TAG, "onResume ENTERS de verdad");
            //Restore value of members from saved state
            cards = (Card[]) recoveryInstance.getParcelableArray(CARDS);
            for (Card item : cards) {
                item.init(this);
            }
            visibleCard = recoveryInstance.getParcelable(VISIBLE_CARD);
            if (visibleCard != null) {
                visibleCard.init(this);
            }
            disabledCards = recoveryInstance.getIntegerArrayList(DISABLED_CARDS);

            tableCard.removeAllViews();
            for (int y = 0; y < TABLE_SIZE; y++) {
                final TableRow row = new TableRow(this);
                for (int x = 0; x < TABLE_SIZE; x++) {
                    row.addView(cards[(y*TABLE_SIZE) + x].getViewAnimator());
                }

                tableCard.addView(row);
            }
            startTime = recoveryInstance.getLong(TIME);
        }
        restored = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setClass(this, WelcomeActivity.class);
        this.startActivity(intent);

        // transition from splash to main menu
        overridePendingTransition(R.animator.splashfadein,
                R.animator.splashfadeout);
    }

    /*Private methods*/
    private Card[] createCells(final int size){
        final Card[] cardSArray = new Card[size];
        for (int i = 0; i < cardSArray.length; i++) {
            Card item = new Card(i, FLAG_RESOURCES[i/2], this);
            cardSArray[i] = item;
        }
        Collections.shuffle(Arrays.asList(cardSArray));
        return cardSArray;
    }

    /*Public methods*/
    public void onDiscoverCard(final Card selectedCard) {
        if (visibleCard == null) {
            visibleCard = selectedCard;
            visibleCard.setVisible(true);
        } else if (visibleCard.getImage() == selectedCard.getImage()) {
            selectedCard.setVisible(true);
            selectedCard.setEnabled(false);
            visibleCard.setEnabled(false);
            disabledCards.add(selectedCard.getId());
            disabledCards.add(visibleCard.getId());
            visibleCard = null;
            if(disabledCards.size() == cards.length){
                Map response = UserHelper.getInstance(MemoramaActivity.this).checkAndUpdateScore(getIntent().getExtras().getLong("userID"), timeInMilliseconds);
                if(response.get("ERROR").equals(UserHelper.NO_ERROR)){
                    Log.e(TAG, "SI ENTRA A VALIDAR");
                    if(response.get("NEW_BEST_SCORE").equals(Boolean.TRUE)){
                        Log.e(TAG, "best score");
                        gameStatus = BEST_SCORE;
                    }else{
                        Log.e(TAG, "YOU WIN");
                        gameStatus = YOU_WIN;
                    }
                }
                gameOver = true;
            }
        } else {
            selectedCard.setVisible(true);
            for(Card item : cards){
                if(!disabledCards.contains(item.getId())){
                    item.getViewAnimator().setEnabled(false);
                }
            }
            mHandler.postDelayed(new Runnable() {
                public void run() {
                        visibleCard.setVisible(false);
                        selectedCard.setVisible(false);
                        visibleCard = null;
                        for(Card item : cards){
                            if(!disabledCards.contains(item.getId())){
                                item.getViewAnimator().setEnabled(true);
                            }
                        }
                }
            }, 1500);
        }
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            int secs = (int) (timeInMilliseconds / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (timeInMilliseconds % 1000);

            timer.setText("" + mins + ":"
                    + String.format("%02d", secs));
            if(timeInMilliseconds > 3*60*1000) { //More than 15 minutes
                gameStatus = YOU_LOSE;
                gameOver = true;
            }

            if(!gameOver){
                timeHandler.postDelayed(this, 0);
            }else{
                showGameOver(gameStatus);
            }
        }
    };

    private void showGameOver(int status){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MemoramaActivity.this);
        alertDialogBuilder.setTitle("GAME OVER");
        switch (status){
            case YOU_LOSE:
                alertDialogBuilder.setIcon(R.drawable.you_lose);
                alertDialogBuilder.setMessage("Sorry, the time is out!");
                break;
            case YOU_WIN:
                alertDialogBuilder.setIcon(R.drawable.you_win);
                alertDialogBuilder.setMessage("Congratulations, you win!");
                break;
            case BEST_SCORE:
                alertDialogBuilder.setIcon(R.drawable.best_score);
                alertDialogBuilder.setMessage("Your new best score is " + String.valueOf(timeInMilliseconds/1000) + " seconds!");
                break;
        }

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        askForNewGame();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void askForNewGame(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MemoramaActivity.this);
        alertDialogBuilder.setTitle("Do you want to play again!")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        initGame();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        intent.setClass(MemoramaActivity.this, WelcomeActivity.class);

                        MemoramaActivity.this.startActivity(intent);
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void initGame(){
        cards = createCells(TABLE_SIZE * TABLE_SIZE);
        visibleCard = null;
        disabledCards = new ArrayList<>();
        startTime = SystemClock.uptimeMillis();

        recoveryInstance = null;
        restored = false;
        startTime = SystemClock.uptimeMillis();
        timeInMilliseconds = 0L;
        gameStatus = 0;
        gameOver = false;

        tableCard.removeAllViews();
        tableCard = (TableLayout) findViewById(R.id.cardGrid);
        for (int y = 0; y < TABLE_SIZE; y++) {
            final TableRow row = new TableRow(this);
            for (int x = 0; x < TABLE_SIZE; x++) {
                row.addView(cards[(y*TABLE_SIZE) + x].getViewAnimator());
            }

            tableCard.addView(row);
        }

        timeHandler.postDelayed(updateTimerThread, 0);
    }


}