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

import com.example.refresh.adapter.ShopItemListAdapter;
import com.example.refresh.database.AppDatabase;
import com.example.refresh.database.model.ShopItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ShoppingFragment extends Fragment implements ShopItemListAdapter.OnShopItemClick{

    private List<ShopItem> shopItemList = new ArrayList<>();

    private TextView shopItemListEmptyTextView;
    private RecyclerView recyclerView;
    private ShopItemListAdapter shopItemListAdapter;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shopping, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Shop Item List");
        db = AppDatabase.getAppDatabase(getContext());
        shopItemListEmptyTextView = rootView.findViewById(R.id.emptyListTextView);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        shopItemListAdapter = new ShopItemListAdapter(getActivity(), shopItemList, this);
        recyclerView.setAdapter(shopItemListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), AddShopItemActivity.class), 100);
            }
        });
        new ShoppingFragment.RetrieveTask(this).execute();
        return rootView;
    }

    private static class RetrieveTask extends AsyncTask<Void, Void, List<ShopItem>> {

        private WeakReference<ShoppingFragment> c;

        // only retain a weak reference to the activity
        RetrieveTask(ShoppingFragment context) {
            c = new WeakReference<>(context);
        }

        @Override
        protected List<ShopItem> doInBackground(Void... voids) {
            if (c.get() != null)
                return c.get().db.shopItemDAO().getAll();
            else
                return null;
        }

        @Override
        protected void onPostExecute(List<ShopItem> items) {
            if (items != null && items.size() > 0) {
                c.get().shopItemList.clear();
                c.get().shopItemList.addAll(items);
                // hides empty text view
                c.get().shopItemListEmptyTextView.setVisibility(View.GONE);
                c.get().shopItemListAdapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode > 0) {
            int pos = data.getIntExtra("position", -1);
            if (resultCode == 1) {
                shopItemList.add((ShopItem) data.getSerializableExtra("shop_item"));
            } else if (resultCode == 2) {
                if(pos != -1) {
                    shopItemList.set(pos, (ShopItem) data.getSerializableExtra("shop_item"));
                }
            }
            listVisibility();
        }
    }

    private void listVisibility() {
        int emptyMsgVisibility = View.GONE;
        if (shopItemList.size() == 0) { // no item to display
            if (shopItemListEmptyTextView.getVisibility() == View.GONE)
                emptyMsgVisibility = View.VISIBLE;
        }
        shopItemListEmptyTextView.setVisibility(emptyMsgVisibility);
        shopItemListAdapter.notifyDataSetChanged();
    }


    @Override
    public void onShopItemClick(int pos) {
        //TODO: Display recipes based on selected shop item.
    }


}
