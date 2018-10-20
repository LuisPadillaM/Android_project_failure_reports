package com.example.luispadilla.projecto_averias.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.luispadilla.projecto_averias.R;
import com.example.luispadilla.projecto_averias.utils.PreferenceManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class AddUserFragment extends Fragment {

    private Unbinder unbinder;
    @BindView(R.id.add_user_name) EditText userName;
    @BindView(R.id.add_user_email) EditText userEmail;
    @BindView(R.id.add_user_phone) EditText userPhone;
    @BindView(R.id.add_user_idCard) EditText userIdCard;
    @BindView(R.id.add_user_username) EditText userUserName;
    @BindView(R.id.add_user_password) EditText userPassword;
    @BindView(R.id.add_user_passwordConfirm) EditText userPasswordConfirm;


    private AddUserInteractionListener mListener;

    public AddUserFragment() {
        // Required empty public constructor
    }

    public static AddUserFragment newInstance() {
        AddUserFragment fragment = new AddUserFragment();
        return fragment;
    }

    @OnClick(R.id.btn_add_user)
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.addUser();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddUserInteractionListener) {
            mListener = (AddUserInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_user, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
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


    public interface AddUserInteractionListener {
        void addUser();
    }
}
