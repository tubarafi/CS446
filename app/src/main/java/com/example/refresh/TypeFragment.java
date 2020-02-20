package com.example.refresh;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import andriod.example.refresh.adapter.TypeListAdapter;
import andriod.example.refresh.cruddialog.AlertTypeCreateDialogFragment;
import andriod.example.refresh.cruddialog.AlertTypeCreateListener;
import andriod.example.refresh.database.AppDatabase;
import andriod.example.refresh.database.model.AlertType;

public class TypeFragment extends Fragment implements AlertTypeCreateListener {

    private List<AlertType> alertTypeList = new ArrayList<>();

    private TextView alertTypeListEmptyTextView;
    private RecyclerView recyclerView;
    private TypeListAdapter typeListAdapter;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_type, container, false);
        db = AppDatabase.getAppDatabase(getContext());
        alertTypeListEmptyTextView = rootView.findViewById(R.id.emptyListTextView);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        typeListAdapter = new TypeListAdapter(getActivity(), alertTypeList);
        recyclerView.setAdapter(typeListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        viewVisibility();
        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAlertTypeCreateDialog();
            }
        });
        new RetrieveTask(this).execute();
        return rootView;
    }



    private static class RetrieveTask extends AsyncTask<Void, Void, List<AlertType>> {

        private WeakReference<TypeFragment> c;

        // only retain a weak reference to the activity
        RetrieveTask(TypeFragment context) {
            c = new WeakReference<>(context);
        }

        @Override
        protected List<AlertType> doInBackground(Void... voids) {
            if (c.get() != null)
                return c.get().db.alertTypeDAO().getAll();
            else
                return null;
        }

        @Override
        protected void onPostExecute(List<AlertType> notes) {
            if (notes != null && notes.size() > 0) {
                c.get().alertTypeList.clear();
                c.get().alertTypeList.addAll(notes);
                // hides empty text view
                c.get().alertTypeListEmptyTextView.setVisibility(View.GONE);
                c.get().typeListAdapter.notifyDataSetChanged();
            }
        }
    }

    private void viewVisibility() {
        if(alertTypeList.isEmpty())
            alertTypeListEmptyTextView.setVisibility(View.VISIBLE);
        else
            alertTypeListEmptyTextView.setVisibility(View.GONE);
    }

    private void openAlertTypeCreateDialog() {
        AlertTypeCreateDialogFragment alertTypeCreateDialogFragment = AlertTypeCreateDialogFragment.newInstance("Create Alert Type", this);
        alertTypeCreateDialogFragment.show(getActivity().getSupportFragmentManager(), "create_alert_type");
    }

    @Override
    public void onAlertTypeCreated(AlertType alertType) {
        alertTypeList.add(alertType);
        typeListAdapter.notifyDataSetChanged();
        viewVisibility();
        Log.d("create alert", alertType.getName());
    }
}
