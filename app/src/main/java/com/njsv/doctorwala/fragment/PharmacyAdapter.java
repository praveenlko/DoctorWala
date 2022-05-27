package com.njsv.doctorwala.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import androidx.recyclerview.widget.RecyclerView;

import com.njsv.doctorwala.R;

import com.njsv.doctorwala.util.ApplicationConstants;
import com.njsv.doctorwala.util.MyGlide;

import java.util.List;

public class PharmacyAdapter extends RecyclerView.Adapter<PharmacyAdapter.ViewHolder> {

    private final Context context;
    private final List<PharmacyOrderModel> pharmacyModelList;


    public PharmacyAdapter(Context context, List<PharmacyOrderModel> pharmacyModelList) {
        this.context = context;
        this.pharmacyModelList = pharmacyModelList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pharmacy_layout, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String pharStatus = pharmacyModelList.get(position).getStatus();
        holder.pharmacyStatus.setText(pharStatus);
        holder.pharmacyId.setText(" : "+pharmacyModelList.get(position).getPharOrderId());
        holder.name.setText(" : "+pharmacyModelList.get(position).getName());
        holder.mobile.setText(" : "+pharmacyModelList.get(position).getMobile());
        holder.address.setText(" : "+pharmacyModelList.get(position).getAddress());
        holder.desc.setText(" : "+pharmacyModelList.get(position).getMsg());
        String date = pharmacyModelList.get(position).getDate();
        String id = pharmacyModelList.get(position).getPharOrderId();
        MyGlide.with(context, ApplicationConstants.INSTANCE.PROFILE_VIEW_IMAGES + pharmacyModelList.get(position).getPharmacyImg(), holder.imageView);


        holder.pharmacyCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(holder.itemView.getContext());
                dialog.setContentView(R.layout.order_cancel_reason);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                EditText cancelReason = dialog.findViewById(R.id.order_cancel_reason);
                Button btnCancelReason = dialog.findViewById(R.id.btn_order_cancel);
                TextView dialogCancel = dialog.findViewById(R.id.dialog_cancel);
                dialogCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnCancelReason.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onClick(View v) {
                        if (!cancelReason.getText().toString().isEmpty()) {

                            PharmacyFragment.pharmacyCancel(id, escapeMetaCharacters(cancelReason.getText().toString()), context);

                            notifyItemChanged(position);
                            notifyDataSetChanged();

                            dialog.dismiss();
                        } else {
                            cancelReason.setError("Please provide any reason");
                        }
                    }
                });

                dialog.show();
            }
        });

        switch (pharStatus) {
            case "pendding":
                holder.orderedIndicator.setColorFilter(Color.rgb(34, 139, 34));
                holder.pharmacyCancel.setBackground(context.getResources().getDrawable(R.drawable.btn_text));
                holder.pharmacyCancel.setPadding(15, 10, 15, 10);
                holder.pharmacyCancel.setText("Cancel");
                holder.pharmacyCancel.setTextColor(Color.rgb(255, 255, 255));
                holder.pharmacyCancel.setEnabled(true);
                holder.pharmacyStatus.setTextColor(Color.rgb(25, 135, 84));

                break;
            case "Confirm":
                holder.message.setText("Your Medicine ready for dispatch");
                holder.orderedIndicator.setColorFilter(Color.rgb(34, 139, 34));
                holder.packedIndicator.setColorFilter(Color.rgb(34, 139, 34));
                holder.O_P_progress.setProgress(100);

                holder.pharmacyCancel.setBackground(context.getResources().getDrawable(R.drawable.btn_text));
                holder.pharmacyCancel.setPadding(15, 10, 15, 10);
                holder.pharmacyCancel.setText("Cancel");
                holder.pharmacyCancel.setTextColor(Color.rgb(255, 255, 255));
                holder.pharmacyCancel.setEnabled(true);
                holder.pharmacyStatus.setTextColor(Color.rgb(25, 135, 84));
                break;

            case "Reject":
                holder.linearLayout.setVisibility(View.INVISIBLE);
                holder.message.setText("Your Prescription is not valid.");

                holder.pharmacyCancel.setBackgroundResource(0);
                holder.pharmacyCancel.setText("Please upload a valid Prescription");
                holder.pharmacyCancel.setTextColor(Color.rgb(255, 0, 0));
                holder.pharmacyCancel.setEnabled(false);
                holder.pharmacyStatus.setTextColor(Color.rgb(255, 0, 0));

                break;

            case "Cancelled":
                holder.linearLayout.setVisibility(View.INVISIBLE);
                holder.pharmacyCancel.setBackgroundResource(0);
                holder.pharmacyCancel.setText("Your Prescription is cancel");
                holder.pharmacyCancel.setTextColor(Color.rgb(255, 0, 0));
                holder.pharmacyCancel.setEnabled(false);
                holder.pharmacyStatus.setTextColor(Color.rgb(255, 0, 0));
                break;

            case "Done":
                holder.orderedIndicator.setColorFilter(Color.rgb(34, 139, 34));
                holder.packedIndicator.setColorFilter(Color.rgb(34, 139, 34));
                holder.shippedIndicator.setColorFilter(Color.rgb(34, 139, 34));
                holder.O_P_progress.setProgress(100);
                holder.P_S_progress.setProgress(100);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return pharmacyModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final TextView address;
        private final TextView message;
        private final TextView name;
        private final TextView mobile;
        private final TextView desc;
        private final TextView pharmacyCancel;
        private final TextView pharmacyStatus;
        private final TextView pharmacyId;
        private final ImageView orderedIndicator;
        private final ImageView packedIndicator;
        private final ImageView shippedIndicator;
        private final ProgressBar O_P_progress;
        private final ProgressBar P_S_progress;
        private final LinearLayout linearLayout;

        public ViewHolder(@NonNull View view) {
            super(view);

            address = itemView.findViewById(R.id.pharmacy_address);
            message = itemView.findViewById(R.id.textView7);
            name = itemView.findViewById(R.id.pharmacy_name);
            mobile = itemView.findViewById(R.id.pharmacy_mobile);
            desc = itemView.findViewById(R.id.pharmacy_desc);
            pharmacyCancel = itemView.findViewById(R.id.pharmacy_cancel);
            pharmacyStatus = itemView.findViewById(R.id.phar_status);
            pharmacyId = itemView.findViewById(R.id.pharmacy_id);

            imageView = itemView.findViewById(R.id.imageView6);
            linearLayout = itemView.findViewById(R.id.linear);

            orderedIndicator = view.findViewById(R.id.ordered_indicator);
            packedIndicator = view.findViewById(R.id.packed_indicator);
            shippedIndicator = view.findViewById(R.id.shipped_indicator);

            O_P_progress = view.findViewById(R.id.order_packed_progress);
            P_S_progress = view.findViewById(R.id.packed_shipped_progress);


        }
    }

    public String escapeMetaCharacters(String inputString) {
        final String[] metaCharacters = {"\\", "^", "$", "{", "}", "[", "]", "(", ")", ".", "*", "+", "?", "|", "<", ">", "-", "&", "%", "'"};

        for (int i = 0; i < metaCharacters.length; i++) {
            if (inputString.contains(metaCharacters[i])) {
                inputString = inputString.replace(metaCharacters[i], "\\" + metaCharacters[i]);
            }
        }
        return inputString;
    }

}