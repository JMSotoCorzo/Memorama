package com.example.soto.memoramaprueba.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.ViewAnimator;

import com.example.soto.memoramaprueba.MemoramaActivity;
import com.example.soto.memoramaprueba.R;
import com.example.soto.memoramaprueba.animation.AnimationFactory;

/**
 * Class to represent a card element in the game.
 *
 * @author Jose Soto
 */
public class Card implements Parcelable {

    private static final String TAG = Card.class.getName();

    private ViewAnimator viewAnimator;
    private ImageView front;
    private ImageView reverse;
    private int image;
    private boolean visible = false;
    private int id;
    private boolean enabled = true;

    public Card(int id, int image, MemoramaActivity container) {
        this.image = image;
        this.id = id;
        init(container);
    }

    /**
     * Initializes the card's settings
     * @param container container context for the view animator instance
     */
    public void init(final MemoramaActivity container) {

        //Create the animator
        viewAnimator = new ViewAnimator(container);
        //Set table row layout params to the animator in order to be able to put inside the a table in the activity
        viewAnimator.setLayoutParams(new TableRow.LayoutParams
                (TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        viewAnimator.setPadding(10, 10, 10, 10);
        //Create layout params for the image views inside the animator
        FrameLayout.LayoutParams lpc = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        //Create the front image view
        front = new ImageView(container);
        front.setLayoutParams(lpc);
        front.setImageResource(R.drawable.circle);
        viewAnimator.addView(front);
        //Create the reverse image view
        reverse = new ImageView(container);
        reverse.setLayoutParams(lpc);
        reverse.setImageResource(image);
        viewAnimator.addView(reverse);

        displayedChildNoAnim(viewAnimator, visible ? 1 : 0);
        this.setEnabled(enabled);

        /**
         * Bind a click listener to initiate the flip transitions
         */
        viewAnimator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!visible) {
                    container.onDiscoverCard(Card.this);
                }
            }

        });
    }

    /**
     * Shows the card using animation factory instance
     */
    public void showCard() {
        AnimationFactory.flipTransition(viewAnimator, AnimationFactory.Direction.LEFT_TO_RIGHT);
    }

    /**
     * Hides the card using animation factory instance
     */
    public void hideCard() {
        AnimationFactory.flipTransition(viewAnimator, AnimationFactory.Direction.RIGHT_TO_LEFT);
    }

    /*Getters and Setters*/
    public void setVisible(boolean value) {
        this.visible = value;
        if (this.visible) {
            showCard();
        } else {
            hideCard();
        }
    }

    public void setEnabled(boolean value) {
        this.enabled = value;
        this.viewAnimator.setEnabled(value);
    }

    public ViewAnimator getViewAnimator() {
        return viewAnimator;
    }

    public int getImage() {
        return image;
    }

    public int getId() {
        return id;
    }

    protected Card(Parcel in) {
        image = in.readInt();
        visible = in.readByte() != 0x00;
        id = in.readInt();
        enabled = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(image);
        dest.writeByte((byte) (visible ? 0x01 : 0x00));
        dest.writeInt(id);
        dest.writeInt((byte) (enabled ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Card> CREATOR = new Parcelable.Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    /**
     * Allows show an specified child inside a ViewAnimator instance without an animation effect.
     * @param parent instance of ViewAnimator
     * @param whichChild number of the view that is a child of the viewAnimator.
     */
    private void displayedChildNoAnim(ViewAnimator parent, int whichChild) {
        Animation inAnimation = parent.getInAnimation();
        Animation outAnimation = parent.getOutAnimation();
        parent.setInAnimation(null);
        parent.setOutAnimation(null);
        parent.setDisplayedChild(whichChild);
        parent.setInAnimation(inAnimation);
        parent.setOutAnimation(outAnimation);
    }
}
