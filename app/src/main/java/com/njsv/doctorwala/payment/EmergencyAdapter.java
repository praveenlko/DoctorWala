package com.njsv.doctorwala.payment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.njsv.doctorwala.R;

import java.util.List;

public class EmergencyAdapter extends RecyclerView.Adapter<EmergencyAdapter.Holder>{

    List<TimeSlotModel> emergencyModelList;
    private int lastSelectedPosition = -1;
    private OnTimeSlotSelected onTimeSlotSelected;
    private String slot;

    public EmergencyAdapter(List<TimeSlotModel> emergencyModelList, OnTimeSlotSelected onTimeSlotSelected) {
        this.emergencyModelList = emergencyModelList;
        this.onTimeSlotSelected = onTimeSlotSelected;
    }

    @NonNull
    @Override
    public EmergencyAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.non_emergency, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmergencyAdapter.Holder holder, int position) {

        String stime =emergencyModelList.get(position).getStartTime();
        String etime =emergencyModelList.get(position).getEndTime();

        holder.startTime.setText(stime);
        holder.endTime.setText(etime);

        holder.emergencyRadio.setChecked(lastSelectedPosition == position);

        if (lastSelectedPosition == position){
            slot = stime+" to "+etime;
            onTimeSlotSelected.onTimeSlot(slot,position);
            Log.d("select","select button "+slot);
        }

    }

    @Override
    public int getItemCount() {
        return emergencyModelList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView startTime,endTime;
        private RadioButton emergencyRadio;
        private String select;
        public Holder(@NonNull View itemView) {
            super(itemView);

            startTime = itemView.findViewById(R.id.start_time);
            endTime = itemView.findViewById(R.id.end_time);
            emergencyRadio = itemView.findViewById(R.id.emergency_radio);

            emergencyRadio.setOnClickListener(view -> {
                lastSelectedPosition = getAdapterPosition();
                select = slot;
                notifyDataSetChanged();
            });

        }
    }

   public interface OnTimeSlotSelected{
        void onTimeSlot(String slot,int position);
   }

}
