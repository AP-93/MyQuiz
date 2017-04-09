package com.example.ante.flagquiz.Data;


import android.provider.BaseColumns;

//final because its class for providing constants
public final class QuestionContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private QuestionContract() {}

    /* Inner class that defines the table contents */
    public static class QuestionEntry implements BaseColumns {


        public static final String TABLE_NAME = "Question";

        // Unique ID number for the question      Type: INTEGER
        public final static String _ID = BaseColumns._ID;

        // Name of image in drawable folder       Type: TEXT
        public static final String COLUMN_NAME_IMAGE = "Image";

        //answers   Type: TEXT
        public static final String COLUMN_NAME_ANSWERA = "AnswerA";
        public static final String COLUMN_NAME_ANSWERB = "AnswerB";
        public static final String COLUMN_NAME_ANSWERC = "AnswerC";
        public static final String COLUMN_NAME_ANSWERD = "AnswerD";

        //correct answer     Type: TEXT
        public static final String COLUMN_NAME_CORRECTANSWER = "CorrectAnswer";
    }
}



