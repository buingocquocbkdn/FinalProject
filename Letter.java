package com.example.mypc.a4pics_1word;

/**
 * Created by MyPC on 18/04/2017.
 */

public class Letter {
    private int id;
    private String letter;
    private int coin;
    private int currentQuestion;
    private int maxQuestion;

    public Letter(){

    }

    public Letter(int id, String letter, int coin, int currentQuestion, int maxQuestion){
        this.id=id;
        this.letter=letter;
        this.coin=coin;
        this.currentQuestion=currentQuestion;
        this.maxQuestion=maxQuestion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public int getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(int currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public int getMaxQuestion() {
        return maxQuestion;
    }

    public void setMaxQuestion(int maxQuestion) {
        this.maxQuestion = maxQuestion;
    }
}
