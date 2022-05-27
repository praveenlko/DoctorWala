package com.njsv.doctorwala.hosipital;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.njsv.doctorwala.R;
import com.njsv.doctorwala.assistant.AssistantFormActivity;
import com.njsv.doctorwala.model.HosipitalModel;
import com.njsv.doctorwala.pharmacy.PharmacyActivity;
import com.njsv.doctorwala.util.ApplicationConstants;
import com.njsv.doctorwala.util.MyGlide;

import java.util.List;

public class HosipitalListAdapter extends RecyclerView.Adapter<HosipitalListAdapter.MYView>{

    private final Context context;
    private final List<HosipitalModel> list;

    public HosipitalListAdapter(Context context, List<HosipitalModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HosipitalListAdapter.MYView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MYView(LayoutInflater.from(context).inflate(R.layout.hosipital_layout_list, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull HosipitalListAdapter.MYView holder, int position) {

        MyGlide.with(context, ApplicationConstants.INSTANCE.CATEGORY_IMAGES + list.get(position).getImage(), holder.image);

        String name = list.get(position).getName();
        holder.name.setText(name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AssistantFormActivity.class);
                intent.putExtra("hospital",name);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MYView extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView name;
        public MYView(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.hosipital_img);
            name = itemView.findViewById(R.id.hosipital_name);
        }
    }
}
