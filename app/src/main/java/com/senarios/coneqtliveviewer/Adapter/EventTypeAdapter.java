package com.senarios.coneqtliveviewer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.senarios.coneqtliveviewer.Model.GetEventTypeDatum;
import com.senarios.coneqtliveviewer.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EventTypeAdapter extends RecyclerView.Adapter<EventTypeAdapter.ViewHolder> {
    private ArrayList<GetEventTypeDatum> arrayList = new ArrayList<>();
    private Context context;
    private GetEventIds getEventIds;


    public EventTypeAdapter(ArrayList<GetEventTypeDatum> arrayList, Context context, GetEventIds getEventIds) {
        this.arrayList = arrayList;
        this.context = context;
        this.getEventIds = getEventIds;
    }

    public interface GetEventIds{
        void getSelectedEvent(Integer ids);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_type_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        holder.eventCheck.setText(arrayList.get(position).getName());
        holder.eventCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                getEventIds.getSelectedEvent(arrayList.get(position).getId());
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox eventCheck;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            eventCheck = itemView.findViewById(R.id.eventCheck);

        }
    }
}

