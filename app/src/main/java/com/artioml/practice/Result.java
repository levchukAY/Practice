package com.artioml.practice;

/**
 * Created by Artiom L on 29.01.2017.
 */

public class Result {

    int punchType;
    String hand;
    String gloves;
    String glovesWeight;
    String position;
    float speed;
    float reaction;
    float acceleration;
    String date;

    public Result(int punchType, String hand, String gloves, String glovesWeight, String position,
                  float speed, float reaction, float acceleration, String date) {
        this.punchType = punchType;
        this.hand = hand;
        this.gloves = gloves;
        this.glovesWeight = glovesWeight;
        this.position = position;
        this.speed = speed;
        this.reaction = reaction;
        this.acceleration = acceleration;
        this.date = date;
    }
}
