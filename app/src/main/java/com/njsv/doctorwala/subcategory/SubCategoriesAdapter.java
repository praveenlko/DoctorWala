package com.njsv.doctorwala.subcategory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.request.RequestOptions;
import com.njsv.doctorwala.R;
import com.njsv.doctorwala.payment.PaymentActivity;
import com.njsv.doctorwala.util.ApplicationConstants;
import com.njsv.doctorwala.util.MyGlide;

import java.security.MessageDigest;
import java.util.List;

public class SubCategoriesAdapter extends RecyclerView.Adapter {
    private List<SubCategoriesModel> list;
    private Context context;
    private Boolean labRadio;
    private final static int OTHER_CATEGORY = 0;
    private final static int LAB_CATEGORY = 1;

    public SubCategoriesAdapter(List<SubCategoriesModel> list, Context context,Boolean labRadio) {
        this.list = list;
        this.context = context;
        this.labRadio = labRadio;
    }

    @Override
    public int getItemViewType(int position) {
        if(labRadio){
            return LAB_CATEGORY;
        } else{
            return OTHER_CATEGORY;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new MYView(LayoutInflater.from(context).inflate(R.layout.sub_category_item, parent, false));
        switch (viewType){
            case LAB_CATEGORY:
                View labCategoryView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lab_radiology,parent,false);
                return new LabRadioViewHolder(labCategoryView);
            case OTHER_CATEGORY:
                View subCategoryView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_category_item,parent,false);
                return new SubCategoryViewHolder(subCategoryView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(labRadio){
            Glide.with(context).load(ApplicationConstants.INSTANCE.CATEGORY_IMAGES+list.get(position).getImage())
                    .error(R.drawable.sign)
                    .into(((LabRadioViewHolder)holder).labImage);

            ((LabRadioViewHolder)holder).labName.setText(list.get(position).getName());

            ((LabRadioViewHolder)holder).itemView.setOnClickListener(v -> {
                Intent intent=new Intent(context, PaymentActivity.class);
                intent.putExtra("subCategoryId",list.get(position).getId());
                intent.putExtra("subCategoryName", this.list.get(position).getName());
                intent.putExtra("subCategoryLink", this.list.get(position).getImage());
                intent.putExtra("categoryId",list.get(position).getCategory());
                context.startActivity(intent);
            });
        } else{

        MyGlide.with(context.getApplicationContext(),ApplicationConstants.INSTANCE.CATEGORY_IMAGES+list.get(position).getImage(),((SubCategoryViewHolder)holder).imageView);

           ((SubCategoryViewHolder)holder).textView.setText(list.get(position).getName());

           if (list.get(position).getCategory().equals("27")){
               ///////////  hosipital list show
               return;
           }

            ((SubCategoryViewHolder)holder).itemView.setOnClickListener(v -> {
            Intent intent=new Intent(context, PaymentActivity.class);
            intent.putExtra("subCategoryId",list.get(position).getId());
            intent.putExtra("subCategoryName", this.list.get(position).getName());
            intent.putExtra("subCategoryLink", this.list.get(position).getImage());
            intent.putExtra("categoryId",list.get(position).getCategory());
           context.startActivity(intent);
        });
    }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class SubCategoryViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;

        public SubCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imgesview);
            textView=itemView.findViewById(R.id.textview);

        }
    }

    public static class LabRadioViewHolder extends RecyclerView.ViewHolder {

        private ImageView labImage;
        private TextView labName,labCharge;

        public LabRadioViewHolder(@NonNull View itemView) {
            super(itemView);

            labImage=itemView.findViewById(R.id.lab_image);
            labName=itemView.findViewById(R.id.lab_name);
            labCharge=itemView.findViewById(R.id.lab_charge);
        }
    }
}
