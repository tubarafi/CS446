package com.example.refresh.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.refresh.AddFoodItemActivity;
import com.example.refresh.R;
import com.example.refresh.database.AppDatabase;
import com.example.refresh.database.model.FoodItem;

public class FoodItemListAdapter extends RecyclerView.Adapter<FoodItemListAdapter.ListViewHolder> {

    private Context context;
    private List<FoodItem> foodItemList;
    private LayoutInflater layoutInflater;
    private AppDatabase db;

    public FoodItemListAdapter(Context context, List<FoodItem> foodItemList) {
        db = AppDatabase.getAppDatabase(context);
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.foodItemList = foodItemList;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.food_item, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodItemListAdapter.ListViewHolder holder, int position) {
        final int itemPosition = position;
        final FoodItem foodItem = foodItemList.get(position);

        holder.foodNameTextView.setText(foodItem.getName());
        holder.quantityTextView.setText(String.valueOf(foodItem.getQuantity()));
        holder.RemindDateTextView.setText(foodItem.getRemindMeOnDate());

        holder.crossButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Are you sure, You wanted to delete this food item?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                deleteFoodItem(itemPosition);
                            }
                        });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });


        holder.editButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity)context).startActivityForResult(new Intent(context, AddFoodItemActivity.class).putExtra("food_item", foodItemList.get(itemPosition)).putExtra("position", itemPosition), 100);
            }
        });



    }

    private void deleteFoodItem(int position) {
        FoodItem foodItem = foodItemList.get(position);
        try {
            db.foodItemDAO().delete(foodItem);
            foodItemList.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "Food item deleted successfully", Toast.LENGTH_LONG).show();
        } catch (Exception ex){
            Log.e("Delete Food", ex.getMessage());
            Toast.makeText(context, "Food item not deleted. Something wrong!", Toast.LENGTH_LONG).show();
        }
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView foodNameTextView;
        TextView quantityTextView;
        TextView RemindDateTextView;
        ImageView crossButtonImageView;
        ImageView editButtonImageView;

        public ListViewHolder(View itemView) {
            super(itemView);

            foodNameTextView = itemView.findViewById(R.id.foodNameTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            RemindDateTextView = itemView.findViewById(R.id.remindDateTextView);
            crossButtonImageView = itemView.findViewById(R.id.crossImageView);
            editButtonImageView = itemView.findViewById(R.id.editImageView);
        }
    }

    public interface OnFoodItemClick {
        void onFoodItemClick(int pos);
    }

    @Override
    public int getItemCount() {
        return foodItemList.size();
    }
}

