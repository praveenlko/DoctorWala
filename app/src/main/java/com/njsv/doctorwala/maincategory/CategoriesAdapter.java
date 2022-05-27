package com.njsv.doctorwala.maincategory;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.njsv.doctorwala.R;
import com.njsv.doctorwala.hosipital.HosipitalListActivity;
import com.njsv.doctorwala.model.CategoryModel;
import com.njsv.doctorwala.pharmacy.PharmacyActivity;
import com.njsv.doctorwala.subcategory.SubCategoryActivity;
import com.njsv.doctorwala.util.ApplicationConstants;
import com.njsv.doctorwala.util.MyGlide;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MYView> {
    private final Context context;
    private final List<CategoryModel> list;

    public CategoriesAdapter(Context context, List<CategoryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MYView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MYView(LayoutInflater.from(context).inflate(R.layout.categories_item_layout, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapter.MYView holder, @SuppressLint("RecyclerView") int position) {
        MyGlide.with(context, ApplicationConstants.INSTANCE.CATEGORY_IMAGES + list.get(position).getImage(), holder.imageView);

        String categoryName = list.get(position).getName();
        holder.categoryName.setText(list.get(position).getName());

        holder.categoriesItem.setOnClickListener(view -> {

            if (position == 0) {
                Intent hosIntent = new Intent(context, HosipitalListActivity.class);
                hosIntent.putExtra("categoryId", list.get(position).getId());
                hosIntent.putExtra("position", position);
                hosIntent.putExtra("categoryName", categoryName);
                hosIntent.putExtra("labRadio", false);
                context.startActivity(hosIntent);
            } else if (position == 3) {
                Intent pharmacyIntent = new Intent(context, PharmacyActivity.class);
                pharmacyIntent.putExtra("categoryId", list.get(position).getId());
                pharmacyIntent.putExtra("position", position);
                pharmacyIntent.putExtra("categoryName", categoryName);
                pharmacyIntent.putExtra("categoryImage", list.get(position).getImage());
                pharmacyIntent.putExtra("labRadio", false);
                context.startActivity(pharmacyIntent);
            }
            else if (position == 6) {
                Intent labIntent = new Intent(context, SubCategoryActivity.class);
                labIntent.putExtra("categoryId", list.get(position).getId());
                labIntent.putExtra("position", position);
                labIntent.putExtra("categoryName", categoryName);
                labIntent.putExtra("categoryImage", list.get(position).getImage());
                labIntent.putExtra("labRadio", true);
                context.startActivity(labIntent);
            } else if (position == 7) {
                Intent subCategoryIntent = new Intent(context, SubCategoryActivity.class);
                subCategoryIntent.putExtra("categoryId", list.get(position).getId());
                subCategoryIntent.putExtra("position", position);
                subCategoryIntent.putExtra("categoryName", categoryName);
                subCategoryIntent.putExtra("labRadio", false);
                context.startActivity(subCategoryIntent);
            } else if (position == 9) {
                Intent radiologyIntent = new Intent(context, SubCategoryActivity.class);
                radiologyIntent.putExtra("categoryId", list.get(position).getId());
                radiologyIntent.putExtra("position", position);
                radiologyIntent.putExtra("categoryName", categoryName);
                radiologyIntent.putExtra("categoryImage", list.get(position).getImage());
                radiologyIntent.putExtra("labRadio", true);
                context.startActivity(radiologyIntent);
            } else if (position == 8) {
                int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            (Activity) context,
                            new String[]{Manifest.permission.CALL_PHONE},
                            1);
                } else {
                    String mob = context.getResources().getString(R.string.enquiry_mobile);
                    context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mob)));
                }
            } else {
                Intent subCategoryIntent = new Intent(context, SubCategoryActivity.class);
                subCategoryIntent.putExtra("categoryId", list.get(position).getId());
                subCategoryIntent.putExtra("position", position);
                subCategoryIntent.putExtra("categoryName", categoryName);
                subCategoryIntent.putExtra("labRadio", false);
                context.startActivity(subCategoryIntent);
            }

        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MYView extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView categoryName;
        private final CardView categoriesItem;

        public MYView(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.tvName);
            categoryName = itemView.findViewById(R.id.categoryName);
            categoriesItem = itemView.findViewById(R.id.categories_item);
        }
    }
}
