package com.example.refresh.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.refresh.R;
import com.example.refresh.RecipeFragment;
import com.example.refresh.database.AppDatabase;
import com.example.refresh.database.model.RecipeItem;

import java.util.List;

public class RecipeItemListAdapter extends RecyclerView.Adapter<RecipeItemListAdapter.ListViewHolder> {

    private Context context;
    private List<RecipeItem> recipeItemList;
    private LayoutInflater layoutInflater;
    private RecipeFragment recipeFragment;
    private AppDatabase db;

    public RecipeItemListAdapter(Context context, List<RecipeItem> recipeItemList, RecipeFragment recipeFragment) {
        db = AppDatabase.getAppDatabase(context);
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.recipeItemList = recipeItemList;
        this.recipeFragment = recipeFragment;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recipe_item, parent, false);
        return new ListViewHolder(view, recipeFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeItemListAdapter.ListViewHolder holder, int position) {
        final int itemPosition = position;
        final RecipeItem recipeItem = recipeItemList.get(position);

        holder.recipeNameTextView.setText(recipeItem.getName());
    }

    public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView recipeNameTextView;
        private OnRecipeItemListener onRecipeItemListener;

        public ListViewHolder(View itemView, OnRecipeItemListener onRecipeItemListener) {
            super(itemView);
            this.onRecipeItemListener = onRecipeItemListener;
            itemView.setOnClickListener(this);
            recipeNameTextView = itemView.findViewById(R.id.recipeNameTextView);
        }

        @Override
        public void onClick(View view) {
            onRecipeItemListener.onRecipeItemClick(getAdapterPosition());
        }
    }

    public interface OnRecipeItemListener {
        void onRecipeItemClick(int pos);
    }


    @Override
    public int getItemCount() {
        return recipeItemList.size();
    }

}
