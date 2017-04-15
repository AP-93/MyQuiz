package com.apquiz.ante.flagquiz.Data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.apquiz.ante.flagquiz.model.QuestionModel;
import com.apquiz.ante.flagquiz.model.RankingModel;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

//This class provides a simple way to use an existing SQLite database
// Link:  https://github.com/jgilfelt/android-sqlite-asset-helper


public class QuestionDbHelper extends SQLiteAssetHelper {

    // Name of the database file
    private static final String DATABASE_NAME = "questions.db";

    // Database version. If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    //Constructs a new instance of  QuestionDbHelper
    //param context of the app
    //super() calls the parent(SQLiteAssetHelper) constructor
    public QuestionDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //The SQLiteOpenHelper methods onConfigure, onCreate and onDowngrade are not supported by this implementation and have been declared final.

    //CRUD For Table

    //using QuestionMoidel class as  List type so we can store both strings and integers on same list
    public List<QuestionModel> getAllQuestion() {

        List<QuestionModel> listQuestion = new ArrayList<>();
        Cursor c;
        SQLiteDatabase db = this.getReadableDatabase();

           //selects all columns and sorts data in random order
           c = db.rawQuery("SELECT * FROM Question ORDER BY Random()", null);

           for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

               //stores data from database row in variables
               int Id = c.getInt(c.getColumnIndex("_id"));
               String Image = c.getString(c.getColumnIndex("Image"));
               String AnswerA = c.getString(c.getColumnIndex("AnswerA"));
               String AnswerB = c.getString(c.getColumnIndex("AnswerB"));
               String AnswerC = c.getString(c.getColumnIndex("AnswerC"));
               String AnswerD = c.getString(c.getColumnIndex("AnswerD"));
               String CorrectAnswer = c.getString(c.getColumnIndex("CorrectAnswer"));

               //adds variable values to question which is QuestionModel type(we can store both integers and strings in same variable)
               QuestionModel question = new QuestionModel(Id, Image, AnswerA, AnswerB, AnswerC, AnswerD, CorrectAnswer);
               //adds question variable to list
               listQuestion.add(question);
           }

           //close cursor after for loop finish
           c.close();

        db.close();
        return listQuestion;
        }



    //Insert Score to Ranking table
    public void insertScore(int score) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "INSERT INTO Ranking(Score) VALUES("+score+")";
        db.execSQL(query);
        db.close();
    }

    //Get Score and sort ranking
    public List<RankingModel> getRanking() {

        List<RankingModel> listRanking = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c;
            c = db.rawQuery("SELECT * FROM Ranking Order By Score DESC;", null);

            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

                int Id = c.getInt(c.getColumnIndex("id"));
                int Score = c.getInt(c.getColumnIndex("Score"));

                RankingModel ranking = new RankingModel(Id, Score);
                listRanking.add(ranking);
            }
            c.close();


        db.close();
        return listRanking;

    }

//get only highscore
    public int getHighscore(){

           int highscore = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c;
        c = db.rawQuery("SELECT MAX(Score) FROM Ranking;", null);
        c.moveToFirst();
        highscore = c.getInt(0);
        c.close();
        db.close();


        return highscore;
    }

    public void deleteLowScore(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM Ranking WHERE id NOT IN (\n" +
                "    SELECT id FROM Ranking ORDER BY score DESC LIMIT 10);";
        db.execSQL(query);
        db.close();
    }
    public void resetScore(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Ranking", null, null);
        db.close();
    }


    //using QuestionMoidel class as  List type so we can store both strings and integers on same list
    public String getRandomDiduknow() {

       // List<String> listDiduknow = new ArrayList<>();
        String randomDiduknow="";
        Cursor c;
        SQLiteDatabase db = this.getReadableDatabase();

        //selects all columns and sorts data in random order
        c = db.rawQuery("SELECT * FROM DidYouKnow ORDER BY Random() LIMIT 1", null);


            //stores data from database row in variables
           try {
               c.moveToFirst();
               randomDiduknow = c.getString(c.getColumnIndex("diduknow"));




           }catch (Exception e
                   ){}






        //close cursor after for loop finish
        c.close();

        db.close();
        return randomDiduknow;
    }
}