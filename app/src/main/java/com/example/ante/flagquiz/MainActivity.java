package com.example.ante.flagquiz;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    Button btnPlay, btnScore, btnOptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlay = (Button) findViewById(R.id.buttonPlay);
        btnScore = (Button) findViewById(R.id.buttonOptions);
        btnOptions = (Button) findViewById(R.id.buttonRecord);

        buttonEffect(btnPlay);
        buttonEffect(btnOptions);
        buttonEffect(btnScore);



    }

    public void playGame(View view) {
        Intent play = new Intent(this, PlayActivity.class);
        startActivity(play);
    }

    public void displayHigscoreList(View view) {
        Intent openScoreList = new Intent(this, HighScoreActivity.class);
        startActivity(openScoreList);
    }


    public void showRules(View view) {
        Intent openScoreList = new Intent(this, RulesActivity.class);
        startActivity(openScoreList);
    }



    //adds efect on button when clicked
    public static void buttonEffect(View button) {
        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter((Color.parseColor("#5f848484")), PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }
}

