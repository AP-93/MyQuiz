package com.apquiz.ante.flagquiz;

import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.apquiz.ante.flagquiz.Data.QuestionDbHelper;
import com.apquiz.ante.flagquiz.model.QuestionModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PlayActivity extends AppCompatActivity {

    List<QuestionModel> questionPlay = new ArrayList<>();
    QuestionDbHelper dbHelper;
    Button btn1, btn2, btn3, btn4;
    ImageView img;
    int index, score, prog, i, highscore, correctAnswerCounter;
    TextView mTextFieldTimer, mTextFieldScore, mTextFieldBonusScore;
    ProgressBar mProgress;
    CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        dbHelper = new QuestionDbHelper(this);
        questionPlay = dbHelper.getAllQuestion();
        highscore = dbHelper.getHighscore();

        index = 0;
        score = 0;
        correctAnswerCounter = 0;
        i = 0;

        btn1 = (Button) findViewById(R.id.answeA);
        btn2 = (Button) findViewById(R.id.answeB);
        btn3 = (Button) findViewById(R.id.answeC);
        btn4 = (Button) findViewById(R.id.answeD);

        img = (ImageView) findViewById(R.id.rndImg);

        mTextFieldTimer = (TextView) findViewById(R.id.timer);
        mTextFieldScore = (TextView) findViewById(R.id.Score);
        mTextFieldBonusScore = (TextView) findViewById(R.id.bonusScore);

        prog = 600;
        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        mProgress.setProgress(prog);

        //counts 60sec time
        countDownTimer = new CountDownTimer(60000, 100) {

            public void onTick(long millisUntilFinished) {

                prog--;
                mProgress.setProgress(prog);
                mTextFieldTimer.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {

                mProgress.setProgress(0);
                mTextFieldTimer.setText("" + 0);
                dbHelper.insertScore(score);
                String a = "You ran out of time.";
                getFinishDialog(a);

            }
        };


        showQuestion(index);
        getBeforeDialog();


    }

    //onClick method for all answer buttons
    public void nextQuestion(View v) {
        Button clickedButton = (Button) v;

        if (index < questionPlay.size() - 1) {
            //if clicked button answer is equal to correct answer increase score
            if (clickedButton.getText().equals(questionPlay.get(index).getCorrectAnswer())) {
                score = score + 10 * ((correctAnswerCounter / 3) + 1);
                correctAnswerCounter++;
            }
            //for wrong answer score is reduced by 3
            else {
                correctAnswerCounter = 0;

                if (score >= 3)
                    score = score - 3;
                else
                    score = 0;
            }
            index++;


        } else if (index == questionPlay.size() - 1) {
            String msg = "You answered all flag questions.";
            getFinishDialog(msg);
            countDownTimer.cancel();
        }

        mTextFieldBonusScore.setText("BONUS:\n x" + ((correctAnswerCounter / 3) + 1));
        mTextFieldScore.setText("score:\n " + score);
        showQuestion(index);

    }

    //get all answers and image names from  List<QuestionModel> questionPlay

    private void showQuestion(int i) {

        List<String> randomAnswers = new ArrayList<>();

        randomAnswers.add(questionPlay.get(i).getAnswerA());
        randomAnswers.add(questionPlay.get(i).getAnswerB());
        randomAnswers.add(questionPlay.get(i).getAnswerC());
        randomAnswers.add(questionPlay.get(i).getAnswerD());

        Collections.shuffle(randomAnswers);

        int ImageId = getResources().getIdentifier(questionPlay.get(i).getImage(), "drawable", getPackageName());
        img.setBackgroundResource(ImageId);
        btn1.setText(randomAnswers.get(0));
        btn2.setText(randomAnswers.get(1));
        btn3.setText(randomAnswers.get(2));
        btn4.setText(randomAnswers.get(3));


    }

    //restart PlayActivity
    private void restartGame() {
        this.recreate();
    }

    //close PlayActivity
    private void closeGame() {
        this.finish();
    }

    //after time runs out
    private void getFinishDialog(String txt) {
        // Creates a builder for an alert dialog
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(PlayActivity.this);
        //activity which will be inflated as alert dialog
        View mView = getLayoutInflater().inflate(R.layout.activity_play_finish, null);

        TextView totalScore = (TextView) mView.findViewById(R.id.textViewTotalScore);
        TextView peviousHighscore = (TextView) mView.findViewById(R.id.textViewHigscore);
        TextView noFlagsMsg = (TextView) mView.findViewById(R.id.textViewNoFlags);
        TextView newHighscore = (TextView) mView.findViewById(R.id.textViewNewHighscoreMsg);
        Button restartBtn = (Button) mView.findViewById(R.id.restartButton);
        Button menuButton = (Button) mView.findViewById(R.id.goToMenuButton);


        noFlagsMsg.setText(txt);
        totalScore.setText("your score:   " + score);
        peviousHighscore.setText("highscore:  " + highscore);

        if (score > highscore) {
            newHighscore.setText("new highscore!");
        } else
            newHighscore.setText("");

        mBuilder.setView(mView);
        final AlertDialog mDialog = mBuilder.create();
        mDialog.show();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);

        //restarts game when restart btn is clicked
        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame();
                mDialog.dismiss();
            }

        });

        //close playActivity, delete low score in database and dismis alert dialog
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeGame();
                dbHelper.deleteLowScore();
                mDialog.dismiss();

            }

        });

    }


    //popup before game starts
    private void getBeforeDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(PlayActivity.this);
        //activity which will be inflated as alert dialog
        View mView = getLayoutInflater().inflate(R.layout.activity_before_play, null);

        String randomDiduknow;

        final TextView diduknowmsg = (TextView) mView.findViewById(R.id.textViewGameRules);
        final TextView threeSec = (TextView) mView.findViewById(R.id.textView3secCount);
        final Button startbtn = (Button) mView.findViewById(R.id.buttonStartGame);

        randomDiduknow = dbHelper.getRandomDiduknow();
        diduknowmsg.setText(randomDiduknow);

        mBuilder.setView(mView);
        final AlertDialog mDialog = mBuilder.create();
        mDialog.show();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);

        startbtn.setEnabled(true);
        startbtn.setClickable(true);

        final CountDownTimer threeSecTimer = new CountDownTimer(3900, 100) {

            public void onTick(long millisUntilFinished) {

                if (millisUntilFinished / 1000 < 1)
                    threeSec.setText("GO!");
                else
                    threeSec.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {

                mDialog.dismiss();
                countDownTimer.start();
            }
        };

        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threeSecTimer.start();
                startbtn.setEnabled(false);
                startbtn.setClickable(false);
            }
        });


    }

    // close timer when user exits game by back button
    public void onBackPressed() {
        countDownTimer.cancel();
        super.onBackPressed();
    }



}



