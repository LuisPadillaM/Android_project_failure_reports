package com.example.luispadilla.projecto_averias.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.luispadilla.projecto_averias.R;
import com.example.luispadilla.projecto_averias.bd.Failure;
import com.example.luispadilla.projecto_averias.bd.services.ApiServices;
import com.example.luispadilla.projecto_averias.bd.services.FailureService;
import com.example.luispadilla.projecto_averias.ui.crud.FailureDetailFragment;
import com.example.luispadilla.projecto_averias.ui.crud.FailureEditFragment;
import com.example.luispadilla.projecto_averias.utils.Constants;
import com.example.luispadilla.projecto_averias.utils.FragmentUtils;


import retrofit2.Callback;

public class FailureDetailActivity extends AppCompatActivity implements FailureDetailFragment.FailureDetailInteractionListener, FailureEditFragment.FailureEditInteractionListener {

    public FailureService failureServices = ApiServices.getFailureServices();
    FailureDetailFragment detailFragment;

    public Integer FragmentContainer = R.id.fragment_container;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_failure_detail);
        fragmentManager = getSupportFragmentManager();
        if(getIntent() != null){
            String id = getIntent().getStringExtra(Constants.KEY_FAILURE_ID);
            detailFragment = FailureDetailFragment.newInstance(id);
            FragmentUtils.addFragment(fragmentManager, FragmentContainer, detailFragment);
        }
    }

    @Override
    public void getFailure(String failureId, Callback<Failure> callback) {
        failureServices.getFailureById(failureId).enqueue(callback);
    }

    @Override
    public void editFailure(String failureId, Failure updatedFailure, Callback<Failure> callback) {
        this.failureServices.updateFailure(failureId, updatedFailure).enqueue(callback);
    }

    @Override
    public void deleteFailure(String failureId, Callback<Failure> callback) {
        this.failureServices.deleteFailure(failureId).enqueue(callback);
    }
}
