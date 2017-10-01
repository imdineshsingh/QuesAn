package com.quesan.app.model;


/**
 * Created by Dinesh Singh on 8/7/2017.
 */

public class Answer
{
    private String questionId,answerId,answer,userId;
    private long postDate;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getPostDate() {
        return postDate;
    }

    public void setPostDate(long postDate) {
        this.postDate = postDate;
    }
}
