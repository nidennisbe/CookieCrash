package com.example.nidennis.tictactoe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static android.content.res.Configuration.*;

public class MainActivity extends AppCompatActivity {
    private GameActivity mGame;
    private Button mNewGame;
    Button btnXO[];

    MediaPlayer mp;


    private TextView mInfoTextView;
    private TextView mPlayerOneCount;
    private TextView mTieCount;
    private TextView mPlayerTwoCount;
    private TextView mPlayerOneText;
    private TextView mPlayerTwoText;

    private int mPlayerOneCounter = 0;
    private int mTieCounter = 0;
    private int mPlayerTwoCounter = 0;

    private boolean mPlayerOneFirst = true;
    private boolean mIsSinglePlayer = false;
    private boolean mIsPlayerOneTurn = true;
    private boolean mGameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_main);

        mp = MediaPlayer.create(MainActivity.this, R.drawable.music);
        mp.start();


        boolean mGameType = getIntent().getExtras().getBoolean("gameType");

        btnXO = new Button[mGame.getBOARD_SIZE()];
        btnXO[0] = (Button) findViewById(R.id.c1);
        btnXO[1] = (Button) findViewById(R.id.c2);
        btnXO[2] = (Button) findViewById(R.id.c3);
        btnXO[3] = (Button) findViewById(R.id.c4);
        btnXO[4] = (Button) findViewById(R.id.c5);
        btnXO[5] = (Button) findViewById(R.id.c6);
        btnXO[6] = (Button) findViewById(R.id.c7);
        btnXO[7] = (Button) findViewById(R.id.c8);
        btnXO[8] = (Button) findViewById(R.id.c9);


        // setup textviews
        mInfoTextView = (TextView) findViewById(R.id.info);
        mPlayerOneCount = (TextView) findViewById(R.id.txtviewp1);
        mTieCount = (TextView) findViewById(R.id.txtviewtie);
        mPlayerTwoCount = (TextView) findViewById(R.id.txtviewp2);
        mPlayerOneText = (TextView) findViewById(R.id.p1);
        mPlayerTwoText = (TextView) findViewById(R.id.p2);

        // set the initial counter display values
        mPlayerOneCount.setText(Integer.toString(mPlayerOneCounter));
        mTieCount.setText(Integer.toString(mTieCounter));
        mPlayerTwoCount.setText(Integer.toString(mPlayerTwoCounter));


        mGame = new GameActivity();

        // start new game
        startNewGame(mGameType);
    }

    public void resetOnClick(View view) {
        mNewGame = (Button) findViewById(R.id.reset);
        mNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewGame(mIsSinglePlayer);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.one_player:
                startNewGame(mIsSinglePlayer);
                break;
            case R.id.exit_game:
                MainActivity.this.finish();
                break;
        }

        return true;
    }

    private void startNewGame(boolean isSingle) {

        this.mIsSinglePlayer = isSingle;

        mGame.clearBoard();

        for (int i = 0; i < btnXO.length; i++) {
            btnXO[i].setText("");
            btnXO[i].setEnabled(true);
            btnXO[i].setOnClickListener(new ButtonClickListener(i));
            btnXO[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.blank));
        }


        if (mIsSinglePlayer) {
            mPlayerOneText.setText("Player 1:");
            mPlayerTwoText.setText("CPU:");

            if (mPlayerOneFirst) {
                mInfoTextView.setText(R.string.first_human);
                mPlayerOneFirst = false;
            } else {
                mInfoTextView.setText(R.string.turn_computer);
                int move = mGame.getComputerMove();
                setMove(mGame.PLAYER_TWO, move);
                mPlayerOneFirst = true;
            }
        } else {
            mPlayerOneText.setText("Player One:");
            mPlayerTwoText.setText("Player Two:");

            if (mPlayerOneFirst) {
                mInfoTextView.setText(R.string.turn_player_one);
                mPlayerOneFirst = false;
            } else {
                mInfoTextView.setText(R.string.turn_player_two);
                mPlayerOneFirst = true;
            }
        }

        mGameOver = false;
    }


    private class ButtonClickListener implements View.OnClickListener {
        int location;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        public ButtonClickListener(int location) {
            this.location = location;
        }

        public void onClick(View view) {
            if (!mGameOver) {
                if (btnXO[location].isEnabled()) {
                    if (mIsSinglePlayer) {
                        setMove(mGame.PLAYER_ONE, location);

                        int winner = mGame.checkForWinner();

                        if (winner == 0) {
                            mInfoTextView.setText(R.string.turn_computer);
                            int move = mGame.getComputerMove();
                            setMove(mGame.PLAYER_TWO, move);
                            winner = mGame.checkForWinner();
                        }

                        if (winner == 0)
                            mInfoTextView.setText(R.string.turn_human);
                        else if (winner == 1) {
                            builder.setMessage(R.string.result_tie).setTitle("Who is the winner?");
                            AlertDialog dialog =builder.create();
                            dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            startNewGame(mIsSinglePlayer);
                                        }
                                    });
                            dialog.show();
                            mTieCounter++;
                            mTieCount.setText(Integer.toString(mTieCounter));
                            mGameOver = true;
                        } else if (winner == 2) {
                            builder.setMessage(R.string.result_human_wins).setTitle("Who is the winner?");
                            AlertDialog dialog =builder.create();
                            dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            startNewGame(mIsSinglePlayer);
                                        }
                                    });
                            dialog.show();
                            mPlayerOneCounter++;
                            mPlayerOneCount.setText(Integer.toString(mPlayerOneCounter));
                            mGameOver = true;
                        } else {
                            builder.setMessage(R.string.result_android_wins).setTitle("Who is the winner?");
                            AlertDialog dialog =builder.create();
                            dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            startNewGame(mIsSinglePlayer);
                                        }
                                    });
                            dialog.show();
                            mPlayerTwoCounter++;
                            mPlayerTwoCount.setText(Integer.toString(mPlayerTwoCounter));
                            mGameOver = true;
                        }
                    } else {
                        if (mIsPlayerOneTurn)
                            setMove(mGame.PLAYER_ONE, location);
                        else
                            setMove(mGame.PLAYER_TWO, location);

                        int winner = mGame.checkForWinner();

                        if (winner == 0) {
                            if (mIsPlayerOneTurn) {
                                mInfoTextView.setText(R.string.turn_player_two);
                                mIsPlayerOneTurn = false;
                            } else {
                                mInfoTextView.setText(R.string.turn_player_one);
                                mIsPlayerOneTurn = true;
                            }
                        } else if (winner == 1) {
                            mInfoTextView.setText(R.string.result_tie);
                            mTieCounter++;
                            mTieCount.setText(Integer.toString(mTieCounter));
                            mGameOver = true;
                        } else if (winner == 2) {

                            builder.setMessage(R.string.result_player_one_wins).setTitle("Who is the winner?");
                            AlertDialog dialog =builder.create();
                            dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            startNewGame(mIsSinglePlayer);
                                        }
                                    });
                            dialog.show();
                           // mInfoTextView.setText(R.string.result_player_one_wins);
                            mPlayerOneCounter++;
                            mPlayerOneCount.setText(Integer.toString(mPlayerOneCounter));
                            mGameOver = true;
                            mIsPlayerOneTurn = false;
                        } else {
                            builder.setMessage(R.string.result_player_two_wins).setTitle("Who is the winner?");
                            AlertDialog dialog =builder.create();
                            dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            startNewGame(mIsSinglePlayer);
                                        }
                                    });
                            dialog.show();
                            mPlayerTwoCounter++;
                            mPlayerTwoCount.setText(Integer.toString(mPlayerTwoCounter));
                            mGameOver = true;
                            mIsPlayerOneTurn = true;
                        }
                    }
                }
            }
        }
    }


    private void setMove(char player, int location) {
        mGame.setMove(player, location);
        btnXO[location].setEnabled(false);
        if (player == mGame.PLAYER_ONE)
            btnXO[location].setBackgroundDrawable(getResources().getDrawable(R.drawable.cookie));
        else
            btnXO[location].setBackgroundDrawable(getResources().getDrawable(R.drawable.donut));
    }


    @Override
    protected void onStop() {
        super.onStop();
        mp.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mp.start();
    }
}