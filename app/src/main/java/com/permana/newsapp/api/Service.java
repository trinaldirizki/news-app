package com.permana.newsapp.api;

import com.permana.newsapp.model.ArticleResponse;
import com.permana.newsapp.model.SourceResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Service {

    @GET("/v2/sources")
    Call<SourceResponse> getSources(@Query("apiKey") String apiKey);

    @GET("/v2/everything")
    Call<ArticleResponse> getArticles(@Query("sources") String source, @Query("apiKey") String apiKey);
}
