package com.example.refresh;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.*;
import com.android.volley.toolbox.*;
import com.example.refresh.adapter.RecipeItemListAdapter;
import com.example.refresh.database.AppDatabase;
import com.example.refresh.database.model.FoodItem;
import com.example.refresh.database.model.RecipeItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.*;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class RecipeFragment extends Fragment implements RecipeItemListAdapter.OnRecipeItemListener {

    private List<RecipeItem> recipeItemList = new ArrayList<>();
    private TextView recipeItemListEmptyTextView;
    private RecyclerView recyclerView;
    private RecipeItemListAdapter recipeItemListAdapter;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipes, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Recipe List");
        db = AppDatabase.getAppDatabase(getContext());
        recipeItemListEmptyTextView = rootView.findViewById(R.id.emptyRecipeListTextView);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recipeItemListAdapter = new RecipeItemListAdapter(getActivity(), recipeItemList, this);
        recyclerView.setAdapter(recipeItemListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RetrieveRecipes(new WeakReference<>(this));
        return rootView;
    }

    private String getIngredientParams () {
        List<FoodItem> foodItemList = this.db.foodItemDAO().getAll();
        String ingredientParams = "";
        if (foodItemList != null) {
            for (int i = 0; i < foodItemList.size(); i++) {
                if (i > 0) {
                    ingredientParams = ingredientParams.concat(", ");
                }
                ingredientParams = ingredientParams.concat(foodItemList.get(i).getName());
            }
        }
        return ingredientParams;
    }

    public void RetrieveRecipes(final WeakReference<RecipeFragment> fragmentReference) {
        Log.d("ingredients", getIngredientParams());
        if (getIngredientParams().equals("")) {
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(fragmentReference.get().getContext());
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.spoonacular.com")
                .appendPath("recipes")
                .appendPath("findByIngredients")
                .appendQueryParameter("apiKey", "c35d2984cb0a4f4c80e2bb7dbf9aae4a")
                .appendQueryParameter("ingredients", getIngredientParams())
                .appendQueryParameter("number", "25");
        String url = builder.build().toString();

        Response.Listener<String> lol = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray yeethay = new JSONArray(response);
                    for (int i = 0; i < yeethay.length(); i++) {
                        JSONObject yeet = yeethay.getJSONObject(i);
                        RecipeItem recipeItem = new RecipeItem(yeet.getString("title"), null);
                        fragmentReference.get().recipeItemList.add(recipeItem);
                    }
                    if (fragmentReference.get().recipeItemList.size() > 0) {
                        fragmentReference.get().recipeItemListEmptyTextView.setVisibility(View.GONE);
                        fragmentReference.get().recipeItemListAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    Log.d("fuck", e.getMessage());
                }
            }
        };

        Response.ErrorListener whoops = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("help", error.toString());
            }
        };

        StringRequest stringy = new StringRequest(Request.Method.GET, url, lol, whoops);

        queue.add(stringy);
    }


    @Override
    public void onRecipeItemClick(int pos) {
        //TODO: navigate to recipe source url?
    }
}
