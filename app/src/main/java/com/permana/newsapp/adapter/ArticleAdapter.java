package com.permana.newsapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.permana.newsapp.R;
import com.permana.newsapp.model.Article;
import com.permana.newsapp.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> implements Filterable {

    private List<Article> articles;
    private List<Article> articlesFiltered;
    private ArticleAdapterListener listener;

    public ArticleAdapter(List<Article> articles, ArticleAdapterListener listener) {
        this.articles = articles;
        this.articlesFiltered = articles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_article, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        holder.bindViews(position);
    }

    @Override
    public int getItemCount() {
        return articlesFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String filterString = constraint.toString();
                if (filterString.isEmpty()) {
                    articlesFiltered = articles;
                } else {
                    List<Article> newArticles = new ArrayList<>();
                    for (Article a : articles) {
                        if (a.getTitle().toLowerCase().contains(filterString.toLowerCase())) {
                            newArticles.add(a);
                        }
                    }

                    articlesFiltered = newArticles;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = articlesFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                articlesFiltered = (List<Article>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ArticleAdapterListener {
        void onArticleSelected(Article article);
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder {

        ImageView imageThumbnail;
        TextView textTitle, textDescription, textDate;
        View view;

        ArticleViewHolder(View itemView) {
            super(itemView);

            view = itemView;
            imageThumbnail = itemView.findViewById(R.id.image_articleThumbnail);
            textTitle = itemView.findViewById(R.id.text_articleTitle);
            textDescription = itemView.findViewById(R.id.text_articleDescription);
            textDate = itemView.findViewById(R.id.text_articleDate);
        }

        void bindViews(int position) {

            final Article article = articlesFiltered.get(position);
            textTitle.setText(article.getTitle());
            textDescription.setText(article.getDescription());
            Glide.with(view.getContext()).load(article.getImageUrl()).into(imageThumbnail);

            String newDate = DateUtils.convertDate(article.getPublishedDate());
            textDate.setText(newDate);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onArticleSelected(articlesFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
}
