package com.example.ante.flagquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.example.ante.flagquiz.Data.QuestionDbHelper;
import com.example.ante.flagquiz.model.RankingModel;
import java.util.ArrayList;
import java.util.List;

public class HighScoreActivity extends AppCompatActivity {

    TextView score1;
    TextView score2;
    TextView score3;
    TextView score4;
    TextView score5;
    TextView score6;
    TextView score7;
    TextView score8;
    TextView score9;
    TextView score10;
    QuestionDbHelper db;
    List<RankingModel> listRanking = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        score1 = (TextView) findViewById(R.id.textViewScore1);
        score2 = (TextView) findViewById(R.id.textViewScore2);
        score3 = (TextView) findViewById(R.id.textViewScore3);
        score4 = (TextView) findViewById(R.id.textViewScore4);
        score5 = (TextView) findViewById(R.id.textViewScore5);
        score6 = (TextView) findViewById(R.id.textViewScore6);
        score7 = (TextView) findViewById(R.id.textViewScore7);
        score8 = (TextView) findViewById(R.id.textViewScore8);
        score9 = (TextView) findViewById(R.id.textViewScore9);
        score10 = (TextView) findViewById(R.id.textViewScore10);


        db = new QuestionDbHelper(this);

        //get ranking from database,order it and put in list
        listRanking = db.getRanking();
        getScoreFromList();
    }

    //display score from list
    private void getScoreFromList() {

        try {
            score1.setText("1.     " + listRanking.get(0).getScore());
            score2.setText("2.     " + listRanking.get(1).getScore());
            score3.setText("3.     " + listRanking.get(2).getScore());
            score4.setText("4.     " + listRanking.get(3).getScore());
            score5.setText("5.     " + listRanking.get(4).getScore());
            score6.setText("6.     " + listRanking.get(5).getScore());
            score7.setText("7.     " + listRanking.get(6).getScore());
            score8.setText("8.     " + listRanking.get(7).getScore());
            score9.setText("9.     " + listRanking.get(8).getScore());
            score10.setText("10.     " + listRanking.get(9).getScore());
        } catch (Exception e) {}
    }

    public void resetScore(View view) {

        score1.setText("-" );
        score2.setText("-" );
        score3.setText("-" );
        score4.setText("-" );
        score5.setText("-" );
        score6.setText("-" );
        score7.setText("-");
        score8.setText("-");
        score9.setText("-" );
        score10.setText("-");

        db.resetScore();

    }
}
