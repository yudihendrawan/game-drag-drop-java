package com.yudi.uasmobile;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MieInstantAdapter extends RecyclerView.Adapter<MieInstantAdapter.ViewHolder> {
    private List<MieInstantStep> steps;

    public MieInstantAdapter(List<MieInstantStep> steps) {
        this.steps = steps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mie_instant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MieInstantStep step = steps.get(position);
        holder.textViewStep.setText(step.getStepText());
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewStep;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewStep = itemView.findViewById(R.id.textViewStep);
        }
    }
}
