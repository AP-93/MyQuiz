package com.apquiz.ante.flagquiz.model;



public class RankingModel {

        private int Id;
        private int Score;

        public RankingModel(int id, int score) {
            Id = id;
            Score = score;
        }

        public int getId() {
            return Id;
        }

        public void setId(int id) {
            Id = id;
        }

        public int getScore() {
            return Score;
        }

        public void setScore(int score) {
            Score = score;
        }
    }

