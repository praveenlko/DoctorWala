package com.njsv.doctorwala.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.njsv.doctorwala.MainActivity;
import com.njsv.doctorwala.R;
import com.njsv.doctorwala.retrofit.UtilMethods;
import com.njsv.doctorwala.retrofit.mCallBackResponse;
import com.njsv.doctorwala.util.ApplicationConstants;
import com.njsv.doctorwala.util.MyGlide;

import java.util.List;

public class NursingAdapter extends RecyclerView.Adapter<NursingAdapter.ViewHolder>{

    private final Context context;
    private List<NursingModel> nursingModelList;

    public NursingAdapter(Context context, List<NursingModel> nursingModelList) {
        this.context = context;
        this.nursingModelList = nursingModelList;
    }
    @NonNull
    @Override
    public NursingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nursing_history_layout, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.name.setText(nursingModelList.get(position).getName());
        holder.mobile.setText(nursingModelList.get(position).getMobile());
        holder.address.setText(nursingModelList.get(position).getAddress());
        holder.desc.setText(nursingModelList.get(position).getMsg());
        String date = nursingModelList.get(position).getDate();
        String id = nursingModelList.get(position).getNurOrderId();
        holder.nursingId.setText(id);
        holder.docName.setText(nursingModelList.get(position).getDocumentType());
        holder.docNumber.setText(nursingModelList.get(position).getDocumentNo());
        holder.nurStatus.setText(nursingModelList.get(position).getStatus());

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
                    @Override
                    public void onClick(View v) {
                        if (!cancelReason.getText().toString().isEmpty()) {
                            NursingFragment.reqCancel(id, escapeMetaCharacters(cancelReason.getText().toString()),context);

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

        switch (nursingModelList.get(position).getStatus()) {
            case "pendding":
                holder.orderedIndicator.setColorFilter(Color.rgb(34, 139, 34));
                holder.pharmacyCancel.setBackground(context.getResources().getDrawable(R.drawable.btn_text));
                holder.pharmacyCancel.setPadding(15,10,15,10);
                holder.pharmacyCancel.setText("Cancel");
                holder.pharmacyCancel.setTextColor(Color.rgb(255, 255, 255));
                holder.pharmacyCancel.setEnabled(true);
                holder.nurStatus.setTextColor(Color.rgb(0, 0, 0));

                break;
            case "Confirm":
                holder.message.setText("Your Medicine ready for dispatch");
                holder.orderedIndicator.setColorFilter(Color.rgb(34, 139, 34));
                holder.packedIndicator.setColorFilter(Color.rgb(34, 139, 34));
                holder.O_P_progress.setProgress(100);

                holder.pharmacyCancel.setBackground(context.getResources().getDrawable(R.drawable.btn_text));
                holder.pharmacyCancel.setPadding(15,10,15,10);
                holder.pharmacyCancel.setText("Cancel");
                holder.pharmacyCancel.setTextColor(Color.rgb(255, 255, 255));
                holder.pharmacyCancel.setEnabled(true);
                holder.nurStatus.setTextColor(Color.rgb(17, 161, 12));
                break;

            case "Reject":
                holder.linearLayout.setVisibility(View.INVISIBLE);
                holder.message.setText("Your Prescription is not valid.");

                holder.pharmacyCancel.setBackgroundResource(0);
                holder.pharmacyCancel.setText("Please upload a valid Prescription");
                holder.pharmacyCancel.setTextColor(Color.rgb(255, 0, 0));
                holder.pharmacyCancel.setEnabled(false);
                holder.nurStatus.setTextColor(Color.rgb(255, 0, 0));
                break;

            case "Cancelled":
                holder.linearLayout.setVisibility(View.INVISIBLE);
                holder.pharmacyCancel.setBackgroundResource(0);
                holder.pharmacyCancel.setText("Your Prescription is cancel");
                holder.pharmacyCancel.setTextColor(Color.rgb(255, 0, 0));
                holder.pharmacyCancel.setEnabled(false);
                holder.nurStatus.setTextColor(Color.rgb(255, 0, 0));

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
        return nursingModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nursingId,address, message, name, mobile, desc, pharmacyCancel,docName,docNumber,nurStatus;
        private ImageView orderedIndicator, packedIndicator, shippedIndicator;
        private ProgressBar O_P_progress, P_S_progress;
        private LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nursingId = itemView.findViewById(R.id.nur_order_id);
            address = itemView.findViewById(R.id.pharmacy_address);
            docName = itemView.findViewById(R.id.nursing_document_name);
            docNumber = itemView.findViewById(R.id.nursing_document_number);
            message = itemView.findViewById(R.id.textView7);
            name = itemView.findViewById(R.id.pharmacy_name);
            mobile = itemView.findViewById(R.id.pharmacy_mobile);
            desc = itemView.findViewById(R.id.pharmacy_desc);
            pharmacyCancel = itemView.findViewById(R.id.pharmacy_cancel);
            nurStatus = itemView.findViewById(R.id.nur_status);

            linearLayout = itemView.findViewById(R.id.linear);

            orderedIndicator = itemView.findViewById(R.id.ordered_indicator);
            packedIndicator = itemView.findViewById(R.id.packed_indicator);
            shippedIndicator = itemView.findViewById(R.id.shipped_indicator);

            O_P_progress = itemView.findViewById(R.id.order_packed_progress);
            P_S_progress = itemView.findViewById(R.id.packed_shipped_progress);
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
