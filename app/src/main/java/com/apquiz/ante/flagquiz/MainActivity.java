package com.apquiz.ante.flagquiz;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.apquiz.ante.flagquiz.Data.QuestionDbHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.example.games.basegameutils.BaseGameUtils;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    Button btnPlay, btnScore, btnOptions, btnleader;
    int highscore;
    QuestionDbHelper dbHelper;


    private GoogleApiClient mGoogleApiClient;

    private static int RC_SIGN_IN = 9001;

    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInflow = true;
    private boolean mSignInClicked = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnleader = (Button) findViewById(R.id.show_leaderboard);
        btnPlay = (Button) findViewById(R.id.buttonPlay);
        btnScore = (Button) findViewById(R.id.buttonOptions);
        btnOptions = (Button) findViewById(R.id.buttonRecord);


        findViewById(R.id.show_leaderboard).setOnClickListener(this);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);


        // Create the Google Api Client with access to the Play Games services
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                // add other APIs and scopes here as needed
                .build();


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


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sign_in_button) {
            signInClicked();
        } else if (view.getId() == R.id.sign_out_button) {
            signOutclicked();
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_button).setVisibility(View.GONE);
        } else if (view.getId() == R.id.show_leaderboard) {

            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                sendScoretoLeaderboard();
                startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
                        mGoogleApiClient, getString(R.string.leaderboard_leaderboard)), 1);
            } else {
                signInClicked();

                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                    sendScoretoLeaderboard();
                    startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
                            mGoogleApiClient, getString(R.string.leaderboard_leaderboard)), 1);

                }
            }
        }
    }


    private void sendScoretoLeaderboard() {
        dbHelper = new QuestionDbHelper(this);
        highscore = dbHelper.getHighscore();
        Games.Leaderboards.submitScore(mGoogleApiClient, "CgkIhvHKqesREAIQBg", highscore);

    }


    @Override
    public void onConnected(Bundle connectionHint) {
        // The player is signed in. Hide the sign-in button and allow the
        // player to proceed.
        findViewById(R.id.sign_in_button).setVisibility(View.GONE);
        findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            // already resolving
            return;
        }

        // if the sign-in button was clicked or if auto sign-in is enabled,
        // launch the sign-in flow
        if (mSignInClicked || mAutoStartSignInflow) {
            mAutoStartSignInflow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;

            // Attempt to resolve the connection failure using BaseGameUtils.
            // The R.string.signin_other_error value should reference a generic
            // error string in your strings.xml file, such as "There was
            // an issue with sign-in, please try again later."
            if (!BaseGameUtils.resolveConnectionFailure(this,
                    mGoogleApiClient, connectionResult,
                    RC_SIGN_IN, String.valueOf(R.string.signin_other_error))) {
                mResolvingConnectionFailure = false;
            }
        }

        // Put code here to display the sign-in button
        findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        findViewById(R.id.sign_out_button).setVisibility(View.GONE);
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Attempt to reconnect
        mGoogleApiClient.connect();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }


    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {

        if (resultCode == GamesActivityResultCodes.RESULT_RECONNECT_REQUIRED && requestCode == RC_SIGN_IN) {
            // force a disconnect to sync up state, ensuring that mClient reports "not connected"
            mGoogleApiClient.disconnect();
        }


        if (requestCode == RC_SIGN_IN) {
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
                mGoogleApiClient.connect();
            } else {
                // Bring up an error dialog to alert the user that sign-in
                // failed. The R.string.signin_failure should reference an error
                // string in your strings.xml file that tells the user they
                // could not be signed in, such as "Unable to sign in."
                BaseGameUtils.showActivityResultError(this,
                        requestCode, resultCode, R.string.signin_failure);
            }
        }
    }

    // Call when the sign-in button is clicked
    private void signInClicked() {
        mSignInClicked = true;
        mGoogleApiClient.connect();
    }

    // Call when the sign-out button is clicked
    private void signOutclicked() {
        mSignInClicked = false;
        Games.signOut(mGoogleApiClient);
        mGoogleApiClient.disconnect();
    }


}

