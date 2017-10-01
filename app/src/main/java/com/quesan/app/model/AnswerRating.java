package com.quesan.app.model;

/**
 * Created by Dinesh Singh on 8/20/2017.
 */

public class AnswerRating
{
    private String answerKey,userId;

    public String getAnswerKey() {
        return answerKey;
    }

    public void setAnswerKey(String answerKey) {
        this.answerKey = answerKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public float getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(float ratingValue) {
        this.ratingValue = ratingValue;
    }

    private float ratingValue;


}
