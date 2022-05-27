package com.njsv.doctorwala.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.njsv.doctorwala.MainActivity;
import com.njsv.doctorwala.R;

import com.njsv.doctorwala.orderHistory.OrderHistoryActivity;
import com.njsv.doctorwala.retrofit.UtilMethods;
import com.njsv.doctorwala.retrofit.mCallBackResponse;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    private List<BookingModel> list;
    private Context context;
    private String mobile;

    public BookingAdapter(List<BookingModel> list, Context context, String mobile) {
        this.list = list;
        this.context = context;
        this.mobile = mobile;
    }

    @NonNull
    @Override
    public BookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_history_layout, parent, false);
        return new ViewHolder(view);

    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull BookingAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String orderId = list.get(position).getOrderId();
        String amtTotal = list.get(position).getTotalamount();
        String status = list.get(position).getStatus();
        String service = list.get(position).getService();
        String schedule = list.get(position).getSchedule();
        String sub_category_id = list.get(position).getSubCatId();

        holder.orderIds.setText(": " + orderId);
        holder.totalAmt.setText(amtTotal + " /-");
        holder.service.setText(": "+service);
        holder.schedule.setText(": "+schedule);
        holder.serviceName.setText("Service Name : "+list.get(position).getSname());

        switch (status){
            case "Recieved":
                holder.orderCancel.setBackground(context.getResources().getDrawable(R.drawable.btn_text));
                holder.orderCancel.setPadding(16,14,16,14);
                holder.orderCancel.setText("Order Cancel");
                holder.orderCancel.setTextColor(Color.rgb(255, 255, 255));
                holder.orderCancel.setEnabled(true);
                break;
            case "Cancelled":
                holder.orderCancel.setBackgroundResource(0);
                holder.orderCancel.setText("Your order is cancel");
                holder.orderCancel.setTextColor(Color.rgb(255, 0, 0));
                holder.orderCancel.setEnabled(false);
                break;
            case "Completed":
                holder.orderCancel.setBackgroundResource(0);
                holder.orderCancel.setText("Your order is completed");
                holder.orderCancel.setTextColor(Color.rgb(0, 255, 0));
                holder.orderCancel.setEnabled(false);
                break;
            case "Assign":
                holder.orderCancel.setBackground(context.getResources().getDrawable(R.drawable.btn_text));
                holder.orderCancel.setPadding(16,14,16,14);
                holder.orderCancel.setText("Order Cancel");
                holder.orderCancel.setTextColor(Color.rgb(255, 255, 255));
                holder.orderCancel.setEnabled(true);
                break;
            default:
                return;
        }

        holder.orderCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog =new Dialog(holder.itemView.getContext());
                dialog.setContentView(R.layout.order_cancel_reason);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                EditText cancelReason  = dialog.findViewById(R.id.order_cancel_reason);
                Button btnCancelReason  = dialog.findViewById(R.id.btn_order_cancel);
                TextView dialogCancel  = dialog.findViewById(R.id.dialog_cancel);
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
                            BookingFragment.cancelOrder(orderId,mobile,cancelReason.getText().toString(),position,context);
//
                            notifyItemChanged(position);
                            notifyDataSetChanged();
                            dialog.dismiss();
                        }else{
                            cancelReason.setError("Please provide any reason");
                        }
                    }
                });

                dialog.show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderHistoryActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("subCatId",sub_category_id);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView orderIds, totalAmt,  orderCancel,service,schedule,serviceName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            orderIds = itemView.findViewById(R.id.order_id);
            totalAmt = itemView.findViewById(R.id.total_amt);
            orderCancel = itemView.findViewById(R.id.cancel_order);
            service = itemView.findViewById(R.id.booking_type);
            schedule = itemView.findViewById(R.id.schedule);
            serviceName = itemView.findViewById(R.id.service_name);


        }
    }
}
