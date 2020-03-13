package com.example.refresh;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.refresh.database.model.FoodItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.example.refresh.adapter.FoodItemListAdapter;
import com.example.refresh.database.AppDatabase;

public class HomeFragment extends Fragment implements FoodItemListAdapter.OnFoodItemListener {

    private List<FoodItem> foodItemList = new ArrayList<>();
    private TextView foodItemListEmptyTextView;
    private RecyclerView recyclerView;
    private FoodItemListAdapter foodItemListAdapter;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Food Item List");
        db = AppDatabase.getAppDatabase(getContext());
        foodItemListEmptyTextView = rootView.findViewById(R.id.emptyListTextView);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        foodItemListAdapter = new FoodItemListAdapter(getActivity(), foodItemList, this);
        recyclerView.setAdapter(foodItemListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        FloatingActionButton cameraFab = rootView.findViewById(R.id.fab_camera);
        FloatingActionButton imageFab = rootView.findViewById(R.id.fab_image);

        //TODO: may change to fab menu open or close instead of showing all fabs
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), AddFoodItemActivity.class), 100);
            }
        });

        cameraFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), ScannerActivity.class), 100);
            }
        });

        imageFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), GalleryActivity.class), 100);
            }
        });

        new RetrieveTask(this).execute();
        return rootView;
    }


    private static class RetrieveTask extends AsyncTask<Void, Void, List<FoodItem>> {

        private WeakReference<HomeFragment> c;

        // only retain a weak reference to the activity
        RetrieveTask(HomeFragment context) {
            c = new WeakReference<>(context);
        }

        @Override
        protected List<FoodItem> doInBackground(Void... voids) {
            if (c.get() != null)
                return c.get().db.foodItemDAO().getAll();
            else
                return null;
        }

        @Override
        protected void onPostExecute(List<FoodItem> items) {
            if (items != null && items.size() > 0) {
                c.get().foodItemList.clear();
                c.get().foodItemList.addAll(items);
                // hides empty text view
                c.get().foodItemListEmptyTextView.setVisibility(View.GONE);
                c.get().foodItemListAdapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode > 0) {
            int pos = data.getIntExtra("position", -1);
            if (resultCode == 1) {
                foodItemList.add((FoodItem) data.getSerializableExtra("food_item"));
            } else if (resultCode == 2) {
                if (pos != -1) {
                    foodItemList.set(pos, (FoodItem) data.getSerializableExtra("food_item"));
                }
            }
            else if (resultCode == 3) {
                foodItemList.addAll((List<FoodItem>) data.getSerializableExtra("food_items"));
            }
            listVisibility();
        }
    }

    private void listVisibility() {
        int emptyMsgVisibility = View.GONE;
        if (foodItemList.size() == 0) { // no item to display
            if (foodItemListEmptyTextView.getVisibility() == View.GONE)
                emptyMsgVisibility = View.VISIBLE;
        }
        foodItemListEmptyTextView.setVisibility(emptyMsgVisibility);
        foodItemListAdapter.notifyDataSetChanged();
    }


    @Override
    public void onFoodItemClick(int pos) {
        //TODO: Display recipes based on selected food item.
    }
}

