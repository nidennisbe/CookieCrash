package com.example.nidennis.tictactoe;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class MenuActivity extends AppCompatActivity {
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_menu);
    }


    public void btnPlayer1_Clicked(View view) {
        mp = MediaPlayer.create(MenuActivity.this, R.drawable.click);
        mp.start();
        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
        intent.putExtra("gameType", true);
        startActivityForResult(intent, 0);
    }

    public void btnPlayer2_Clicked(View view) {
        mp = MediaPlayer.create(MenuActivity.this, R.drawable.click);
        mp.start();
        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
        intent.putExtra("gameType", false);
        startActivityForResult(intent, 0);
    }

    public void btnExit_Clicked(View view) {
        mp = MediaPlayer.create(MenuActivity.this, R.drawable.clickexit);
        mp.start();
        MenuActivity.this.finish();
    }

}
