package com.example.luispadilla.projecto_averias.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luispadilla.projecto_averias.R;
import com.example.luispadilla.projecto_averias.bd.Failure;
import com.example.luispadilla.projecto_averias.ui.FailureDetailActivity;
import com.example.luispadilla.projecto_averias.utils.Constants;
import com.example.luispadilla.projecto_averias.utils.Utilities;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FailureListAdapter extends RecyclerView.Adapter<FailureListAdapter.ViewHolder> {

    List<Failure> failures;
    private Context context;

    public FailureListAdapter(Context context, List<Failure> failures){
        this.context = context;
        this.failures = failures;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.failure_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final Failure current = this.getItem(position);
        viewHolder.assignData(current, position);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> intentParams = new HashMap<String, Object>();
                intentParams.put(Constants.KEY_FAILURE_ID, current.id);
                Utilities.SwitchActivity(context, FailureDetailActivity.class, intentParams);
            }
        });
    }

    @Override
    public int getItemCount() {
        return failures.size();
    }

    public Failure getItem(Integer position ) { return failures.get(position); }

    public class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.failure_name) TextView name;
        @BindView(R.id.failure_description) TextView description;
        @BindView(R.id.failure_date) TextView date;
        @BindView(R.id.failure_photo) ImageView photo;
        private Unbinder unbinder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }

        public void assignData(Failure failure, int position) {
            name.setText(failure.name);
            description.setText(failure.description);
            date.setText(failure.date);
            if(!Utilities.isEmptyStringField(failure.image)){
                Picasso.get().load(failure.image).into(photo);
            }


        }
    }
}
