package com.example.luispadilla.projecto_averias.ui.crud;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luispadilla.projecto_averias.R;
import com.example.luispadilla.projecto_averias.bd.Failure;
import com.example.luispadilla.projecto_averias.ui.FailureDetailActivity;
import com.example.luispadilla.projecto_averias.utils.Constants;
import com.example.luispadilla.projecto_averias.utils.FragmentUtils;
import com.example.luispadilla.projecto_averias.utils.Utilities;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FailureDetailFragment extends Fragment {

    FailureDetailActivity activity;
    Failure currentFailure;
    private FailureDetailInteractionListener mListener;
    public FailureEditFragment editFragment;
    private String currentId;
    Context context;
    @BindView(R.id.failure_name) TextView failureName;
    @BindView(R.id.failure_date) TextView failureDate;
    @BindView(R.id.failure_type) TextView failureType;
    @BindView(R.id.failure_description) TextView failureDescription;
    @BindView(R.id.failure_image) ImageView failureImage;



    private Unbinder unbinder;

    public FailureDetailFragment() {
        // Required empty public constructor
    }

    public static FailureDetailFragment newInstance(String id) {
        FailureDetailFragment fragment = new FailureDetailFragment();
        Bundle args = new Bundle();
        args.putString(Constants.KEY_FAILURE_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentId = getArguments().getString(Constants.KEY_FAILURE_ID);
        }
        activity = (FailureDetailActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_failure_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        Callback<Failure> responseCallback = new Callback<Failure>() {
            @Override
            public void onResponse(Call<Failure> call, Response<Failure> response) {
                if(response.errorBody() == null){
                    FailureDetailFragment.this.setupItem(response.body());
                } else {
                    FailureDetailFragment.this.failedGetItem();
                }

            }

            @Override
            public void onFailure(Call<Failure> call, Throwable t) {
                FailureDetailFragment.this.failedGetItem();
            }
        };
        mListener.getFailure(currentId,responseCallback);
        return view;
    }

    @OnClick(R.id.btn_edit)
    public void edit(){
        Bundle params = new Bundle();
        params.putParcelable(Constants.KEY_FAILURE, this.currentFailure);
        editFragment = FailureEditFragment.newInstance(params);
        FragmentUtils.changeFragment(getFragmentManager(), activity.FragmentContainer, editFragment, true );
    }

    public void failedGetItem(){
        Utilities.showToast(FailureDetailFragment.this.getContext(), "Error getting failure item");
    }

    public void setupItem(Failure current) {
        this.currentFailure = current;
        failureName.setText(current.name);
        failureDate.setText(current.date);
        failureType.setText(current.type);
        failureDescription.setText(current.description);
        if(!Utilities.isEmptyStringField(current.image)){
            Picasso.get().load(current.image).into(failureImage);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof FailureDetailInteractionListener) {
            mListener = (FailureDetailInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FailureDetailInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface FailureDetailInteractionListener {
        void getFailure(String failureId, Callback<Failure> callback);
    }
}
