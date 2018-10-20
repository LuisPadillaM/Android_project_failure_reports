package com.example.luispadilla.projecto_averias.ui.crud;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.luispadilla.projecto_averias.R;
import com.example.luispadilla.projecto_averias.bd.Failure;
import com.example.luispadilla.projecto_averias.ui.FaultActivity;
import com.example.luispadilla.projecto_averias.utils.Constants;
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


public class FailureEditFragment extends Fragment {

    private FailureEditInteractionListener mListener;
    // Use shared layout so they are the same as addActivity fields
    @BindView(R.id.failure_name) EditText failureName;
    @BindView(R.id.failure_description) EditText failureDescription;
    @BindView(R.id.failure_type) EditText failureType;
    @BindView(R.id.failure_image) ImageView failurePhoto;
    private Failure current;
    private Unbinder unbinder;
    MaterialDialog removeFailureDialog;

    public FailureEditFragment() {
        // Required empty public constructor
    }


    public static FailureEditFragment newInstance(Bundle args) {
        FailureEditFragment fragment = new FailureEditFragment();
        if(args != null){
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            current = getArguments().getParcelable(Constants.KEY_FAILURE);
        }
    }

    public void initFields(Failure current) {
        failureName.setText(current.name, TextView.BufferType.EDITABLE);
        failureDescription.setText(current.description, TextView.BufferType.EDITABLE);
        failureType.setText(current.type, TextView.BufferType.EDITABLE);
        if(!Utilities.isEmptyStringField(current.image)){
            Picasso.get().load(current.image).into(failurePhoto);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_failure_edit, container, false);
        unbinder = ButterKnife.bind(this, view);
        this.initFields(current);
        removeFailureDialog = this.createRemoveFailureDialog();
        failurePhoto.setImageDrawable(getResources().getDrawable(Constants.failurePlaceHolder));
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FailureEditInteractionListener) {
            mListener = (FailureEditInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FailureEditInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.btn_delete)
    public void deleteFailureModal(View view) {
        removeFailureDialog.getBuilder().show();
    }

    @OnClick(R.id.btn_edit)
    public void editFailure(){
        Failure updated = (Failure) current.regularClone();
        updated.setName(failureName.getText().toString());
        updated.setDescription(failureDescription.getText().toString());
        updated.setType(failureType.getText().toString());

        final Callback<Failure> responseCallback = new Callback<Failure>() {
            @Override
            public void onResponse(Call<Failure> call, Response<Failure> response) {
                if(response.body() == null){
                    FailureEditFragment.this.failedAction();
                } else {
                    Utilities.SwitchActivity(getActivity(), FaultActivity.class,null);
                }

            }

            @Override
            public void onFailure(Call<Failure> call, Throwable t) {
                FailureEditFragment.this.failedAction();
            }
        };
        mListener.editFailure(current.id, updated, responseCallback);
    }

    public MaterialDialog createRemoveFailureDialog(){
        final Callback<Failure> responseCallback = new Callback<Failure>() {
            @Override
            public void onResponse(Call<Failure> call, Response<Failure> response) {
                if(response.body() == null){
                    FailureEditFragment.this.failedAction();
                } else {
                    Utilities.SwitchActivity(getActivity(), FaultActivity.class,null);
                }

            }

            @Override
            public void onFailure(Call<Failure> call, Throwable t) {
                FailureEditFragment.this.failedAction();
            }
        };
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this.getContext())
                .title(R.string.modal_title)
                .content(R.string.modal_content_remove)
                .positiveText(R.string.btn_delete)
                .negativeText(R.string.modal_button_cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        mListener.deleteFailure(current.id,  responseCallback);
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

    public void failedAction(){
        Utilities.showToast(FailureEditFragment.this.getContext(), "Error failed action");
    }

    public interface FailureEditInteractionListener {

        void editFailure(String failureId, Failure updatedFailure, Callback<Failure> callback);
        void deleteFailure(String failureId, Callback<Failure> callback);
    }
}
