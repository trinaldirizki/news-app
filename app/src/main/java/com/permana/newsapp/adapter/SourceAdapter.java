package com.permana.newsapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.permana.newsapp.R;
import com.permana.newsapp.model.Source;

import java.util.List;

public class SourceAdapter extends RecyclerView.Adapter<SourceAdapter.SourceViewHolder> {

    private List<Source> sources;
    private SourceAdapterListener listener;

    public SourceAdapter(List<Source> sources, SourceAdapterListener listener) {
        this.sources = sources;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_source, parent, false);
        return new SourceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SourceViewHolder holder, int position) {
        holder.bindViews(position);
    }

    @Override
    public int getItemCount() {
        return sources != null ? sources.size() : 0;
    }

    public interface SourceAdapterListener {
        void onSourceSelected(Source source);
    }

    class SourceViewHolder extends RecyclerView.ViewHolder {

        TextView textName, textCategory;
        View view;

        SourceViewHolder(View itemView) {
            super(itemView);

            view = itemView;
            textName = itemView.findViewById(R.id.text_sourceName);
            textCategory = itemView.findViewById(R.id.text_sourceCategory);
        }

        void bindViews(int position) {
            final Source source = sources.get(position);
            textName.setText(source.getName());
            textCategory.setText(source.getCategory());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSourceSelected(sources.get(getAdapterPosition()));
                }
            });
        }
    }
}
