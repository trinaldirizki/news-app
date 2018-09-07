package com.permana.newsapp.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.permana.newsapp.ArticleActivity;
import com.permana.newsapp.BuildConfig;
import com.permana.newsapp.R;
import com.permana.newsapp.adapter.SourceAdapter;
import com.permana.newsapp.api.ApiResponse;
import com.permana.newsapp.api.ClientInstance;
import com.permana.newsapp.api.Service;
import com.permana.newsapp.model.Source;
import com.permana.newsapp.model.SourceResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SourceFragment extends Fragment implements ApiResponse, SourceAdapter.SourceAdapterListener {

    private RecyclerView recyclerSource;
    private SourceAdapter adapter;
    private List<Source> sourceList = new ArrayList<>();
    private ProgressDialog progressDialog;

    private OnFragmentInteractionListener mListener;

    public SourceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_source, container, false);

        initRecyclerView(view);
        initDialog(view);
        loadSourceResponse();

        return view;
    }

    private void initDialog(View view) {
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void initRecyclerView(View view) {
        recyclerSource = view.findViewById(R.id.recycler_source);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new SourceAdapter(sourceList, this);
        recyclerSource.setLayoutManager(layoutManager);
        recyclerSource.setAdapter(adapter);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void loadSourceResponse() {
        try {
            if (BuildConfig.NEWS_API.isEmpty()) {
                Toast.makeText(getContext(), "API key needed", Toast.LENGTH_SHORT).show();
                return;
            }

            Service service = ClientInstance.getInstance().create(Service.class);
            Call<SourceResponse> call = service.getSources(BuildConfig.NEWS_API);
            call.enqueue(new Callback<SourceResponse>() {
                @Override
                public void onResponse(Call<SourceResponse> call, Response<SourceResponse> response) {
                    List<Source> sources;
                    if (response.body() != null) {
                        sources = response.body().getSources();
                        sourceList.addAll(sources);
                        adapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<SourceResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error occurred", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void loadArticleResponse() {
        // Required
    }

    @Override
    public void onSourceSelected(Source source) {
        displayArticleActivity(getContext(), source);
    }

    void displayArticleActivity(Context context, Source source) {
        if (context != null) {
            Intent intent = new Intent(context, ArticleActivity.class);
            intent.putExtra("SOURCE_ID", source.getId());
            intent.putExtra("SOURCE_NAME", source.getName());
            context.startActivity(intent);
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
