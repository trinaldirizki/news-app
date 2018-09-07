package com.permana.newsapp;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.permana.newsapp.fragment.SourceFragment;

public class MainActivity extends AppCompatActivity
        implements SourceFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initSourceFragment();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(getString(R.string.pick_source));
        }
    }

    private void initSourceFragment() {
        SourceFragment fragment = new SourceFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.placeholder, fragment).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // Required
    }
}
