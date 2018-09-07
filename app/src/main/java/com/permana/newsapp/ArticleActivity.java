package com.permana.newsapp;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.permana.newsapp.adapter.ArticleAdapter;
import com.permana.newsapp.api.ApiResponse;
import com.permana.newsapp.api.ClientInstance;
import com.permana.newsapp.api.Service;
import com.permana.newsapp.model.Article;
import com.permana.newsapp.model.ArticleResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleActivity extends AppCompatActivity
        implements ArticleAdapter.ArticleAdapterListener, ApiResponse {

    public static final String ARTICLE_URL = "articleUrl";

    private RecyclerView recyclerView;
    private List<Article> articleList;
    private ArticleAdapter adapter;
    private SearchView searchView;
    private ProgressDialog progressDialog;
    private String mSourceId;
    private String mSourceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mSourceId = extras.getString("SOURCE_ID");
            mSourceName = extras.getString("SOURCE_NAME");
        }

        initToolbar();
        initRecyclerView();
        initDialog();

        loadArticleResponse();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_article);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(mSourceName + " - Articles");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        articleList = new ArrayList<>();
        adapter = new ArticleAdapter(articleList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void onArticleSelected(Article article) {
        displayWebView(article);
    }

    private void displayWebView(Article article) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(ARTICLE_URL, article.getUrl());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_article, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }

        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_search) {
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }

        super.onBackPressed();
    }

    @Override
    public void loadSourceResponse() {
        // Required
    }

    @Override
    public void loadArticleResponse() {
        try {

            if (BuildConfig.NEWS_API.isEmpty()) {
                Toast.makeText(this, "API key needed", Toast.LENGTH_SHORT).show();
                return;
            }

            Service service = ClientInstance.getInstance().create(Service.class);
            Call<ArticleResponse> call = service.getArticles(mSourceId, BuildConfig.NEWS_API);
            call.enqueue(new Callback<ArticleResponse>() {
                @Override
                public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
                    List<Article> articles;
                    if (response.body() != null) {
                        articles = response.body().getArticles();
                        articleList.addAll(articles);
                        adapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ArticleResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Error fetching data", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_SHORT).show();
        }
    }
}
