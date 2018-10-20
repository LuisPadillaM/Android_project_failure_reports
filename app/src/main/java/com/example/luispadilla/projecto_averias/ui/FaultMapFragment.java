package com.example.luispadilla.projecto_averias.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.luispadilla.projecto_averias.R;
import com.example.luispadilla.projecto_averias.bd.Failure;
import com.example.luispadilla.projecto_averias.utils.Constants;
import com.example.luispadilla.projecto_averias.utils.Utilities;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FaultMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMapLongClickListener {

    private FaultMapInteractionListener mListener;
    private GoogleMap googleMap;
    Location currentLocation;
    private LatLng initialLocation = new LatLng(9.9328022,-84.0317056);
    View mView;
    private Unbinder unbinder;
    MaterialDialog addFailureDialog;

    public FaultMapFragment() {
        // Required empty public constructor
    }

    public static FaultMapFragment newInstance() {
        FaultMapFragment fragment = new FaultMapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MaterialDialog createAddFailureDialog() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this.getContext())
                .title(R.string.modal_title)
                .content(R.string.modal_content_add)
                .positiveText(R.string.modal_button_create)
                .negativeText(R.string.modal_button_cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        Utilities.SwitchActivity(getActivity(), FailureAddActivity.class, null);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        dialog.dismiss();
                    }
                });

        MaterialDialog dialog = builder.build();
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_fault_map, container, false);
        unbinder = ButterKnife.bind(this, mView);
        Callback<List<Failure>> responseCallback = new Callback<List<Failure>>() {
            @Override
            public void onResponse(Call<List<Failure>> call, Response<List<Failure>> response) {
                FaultMapFragment.this.setupList(response.body());
            }

            @Override
            public void onFailure(Call<List<Failure>> call, Throwable t) {
                Utilities.showToast(FaultMapFragment.this.getContext(), "Error getting failure list");
            }
        };
        mListener.getList(responseCallback);
        // mListener.getUserLocation();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return mView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FaultMapInteractionListener) {
            mListener = (FaultMapInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FaultMapInteractionListener");
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setOnMapLongClickListener(this);
        googleMap.setOnInfoWindowClickListener(this);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 12.0f));
        this.addMarker(initialLocation, "starting point", "", true);
        addFailureDialog = this.createAddFailureDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            this.checkPermission();
        }
    }

    private void checkPermission() {
        int permission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            askForPermission();
        }
    }

    public void askForPermission(){
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.PERM_CODE);
    }

    @Override
    public void onMapLongClick(LatLng position) {
        addFailureDialog.getBuilder().show();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        HashMap<String, Object> intentParams = new HashMap<String, Object>();
        intentParams.put(Constants.KEY_FAILURE_ID, marker.getTag());
        Utilities.SwitchActivity(getActivity(), FailureDetailActivity.class, intentParams);
    }

    public void setupList (List<Failure> failures) {
        for (Failure currentFailure : failures) {
            if(!currentFailure.location.equals(null)){
                LatLng currentPosition = new LatLng(currentFailure.location.lat, currentFailure.location.lat);
                Marker newMarker = this.addMarker(currentPosition, currentFailure.name, currentFailure.description, false);
                newMarker.setTag(currentFailure.id);
            }
        }


    }

    private Marker addMarker (LatLng position, String title, String description, Boolean anim) {

        MarkerOptions markerBuilder = new MarkerOptions().position(position);

        if(!Utilities.isEmptyStringField(title)){
            markerBuilder.title(title);
        }
        if(!Utilities.isEmptyStringField(description)){
            markerBuilder.snippet(description);
        }

        Marker newMarker = googleMap.addMarker(markerBuilder);

        if (anim) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 19.0f));
        }
        return newMarker;
    }
    public interface FaultMapInteractionListener {

        void getList(Callback<List<Failure>> callback);
        Location getUserLocation();
    }
}
