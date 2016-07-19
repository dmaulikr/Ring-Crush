package com.example.jorge.ringcrush;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GameView(this));
    }

    public void gameOver()
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction("JAMV");
        sendIntent.putExtra("letter","J4");
        startActivity(sendIntent);
    }

}
