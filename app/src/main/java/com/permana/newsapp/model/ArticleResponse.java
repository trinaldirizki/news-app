package com.permana.newsapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArticleResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("totalResults")
    private int totalResult;
    @SerializedName("articles")
    private List<Article> articles;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
