package com.example.luispadilla.projecto_averias.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.luispadilla.projecto_averias.R;
import com.example.luispadilla.projecto_averias.adapters.FailureListAdapter;
import com.example.luispadilla.projecto_averias.bd.Failure;
import com.example.luispadilla.projecto_averias.utils.Utilities;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FaultListFragment extends Fragment {

    private FaultListListener mListener;
    @BindView(R.id.recyclerview_failure_list) RecyclerView failureList;
    private Unbinder unbinder;

    public FaultListFragment() {
        // Required empty public constructor
    }

    public static FaultListFragment newInstance(String param1, String param2) {
        FaultListFragment fragment = new FaultListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fault_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        Callback<List<Failure>> responseCallback = new Callback<List<Failure>>() {
            @Override
            public void onResponse(Call<List<Failure>> call, Response<List<Failure>> response) {
                FaultListFragment.this.setupList(response.body());
            }

            @Override
            public void onFailure(Call<List<Failure>> call, Throwable t) {
                Utilities.showToast(FaultListFragment.this.getContext(), "Error getting failure list");
            }
        };
        mListener.getList(responseCallback);
        return view;
    }

    @OnClick(R.id.btn_add_failure)
    public void goToAddFailure(){
        Utilities.SwitchActivity(getContext(), FailureAddActivity.class, null);
    }



    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FaultListListener) {
            mListener = (FaultListListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FaultListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setupList (List<Failure> failures) {
        RecyclerView.LayoutManager displayLayout = new LinearLayoutManager(this.getContext());
        failureList.setLayoutManager(displayLayout);
        failureList.setAdapter(new FailureListAdapter(this.getActivity(), failures));
    }

    public interface FaultListListener {
        void getList(Callback<List<Failure>> callback);
    }
}
