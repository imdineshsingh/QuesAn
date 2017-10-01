package com.quesan.app.model;

/**
 * Created by Dinesh Singh on 8/7/2017.
 */

public class Question
{
    private String questionId,description,title,userId;
    private long postDate;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
