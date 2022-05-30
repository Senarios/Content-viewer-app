package com.senarios.coneqtliveviewer.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.senarios.coneqtliveviewer.Model.ParticipantsModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.senarios.coneqtliveviewer.R;

public class ParticipantsAdapter extends FirebaseRecyclerAdapter<ParticipantsModel,ParticipantsAdapter.ViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ParticipantsAdapter(@NonNull FirebaseRecyclerOptions<ParticipantsModel> options) {
        super(options);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.participants_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull ParticipantsModel model) {
        holder.participant_name.setText(model.getName());
        holder.kickout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("streams").child("987654")
                        .child("participents").child(getRef(position).getKey()).removeValue();
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView participant_name,kickout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            participant_name = itemView.findViewById(R.id.part_name);
            kickout = itemView.findViewById(R.id.kick_out_part);
        }
    }
}
