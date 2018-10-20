package com.example.luispadilla.projecto_averias.ui;

import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.luispadilla.projecto_averias.R;
import com.example.luispadilla.projecto_averias.adapters.ViewPagerAdapter;
import com.example.luispadilla.projecto_averias.bd.Failure;
import com.example.luispadilla.projecto_averias.bd.services.ApiServices;
import com.example.luispadilla.projecto_averias.bd.services.FailureService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Callback;

public class FaultActivity extends AppCompatActivity implements FaultListFragment.FaultListListener, FaultMapFragment.FaultMapInteractionListener {

    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.strip) PagerTabStrip pagerStrip;
    ViewPagerAdapter pageAdapter;
    FailureService failureServices = ApiServices.getFailureServices();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fault);
        ButterKnife.bind(this);
        pageAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);
        viewPager.setOffscreenPageLimit(pageAdapter.getCount());
    }

    @Override
    public void getList(Callback<List<Failure>> callback) {
        failureServices.getAllFailures().enqueue(callback);
    }
}
