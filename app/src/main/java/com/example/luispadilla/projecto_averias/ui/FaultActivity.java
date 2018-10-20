package com.example.luispadilla.projecto_averias.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.luispadilla.projecto_averias.R;
import com.example.luispadilla.projecto_averias.adapters.ViewPagerAdapter;
import com.example.luispadilla.projecto_averias.bd.Failure;
import com.example.luispadilla.projecto_averias.bd.services.ApiServices;
import com.example.luispadilla.projecto_averias.bd.services.FailureService;
import com.example.luispadilla.projecto_averias.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Callback;

public class FaultActivity extends AppCompatActivity implements FaultListFragment.FaultListListener, FaultMapFragment.FaultMapInteractionListener {

    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.strip) PagerTabStrip pagerStrip;
    ViewPagerAdapter pageAdapter;
    LocationManager locationManager;
    FailureService failureServices = ApiServices.getFailureServices();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fault);
        ButterKnife.bind(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        pageAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);
        viewPager.setOffscreenPageLimit(pageAdapter.getCount());
    }

    @Override
    public void getList(Callback<List<Failure>> callback) {
        failureServices.getAllFailures().enqueue(callback);
    }

    public void askLocationPermission() {
        String[] PERMISSIONS = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
        };
        ActivityCompat.requestPermissions(this, PERMISSIONS, Constants.PERM_CODE);
    }

    private Boolean checkPermissions() {
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarsePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        int permissionGranted = PackageManager.PERMISSION_GRANTED;
        return permission == permissionGranted || coarsePermission == permissionGranted;
    }

    @SuppressLint("MissingPermission")
    @Override
    public Location getUserLocation() {
        Location result = null;
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        if (this.checkPermissions()) {
            result = locationManager.getLastKnownLocation(locationProvider);
        } else {
            askLocationPermission();
        }
        return result;
    }
}
