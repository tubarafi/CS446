package com.example.refresh.cruddialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.refresh.R;
import com.example.refresh.database.AppDatabase;
import com.example.refresh.database.model.AlertType;
import com.example.refresh.util.Constants;


public class AlertTypeCreateDialogFragment extends DialogFragment {

    private static AlertTypeCreateListener alertTypeCreateListener;

    private EditText nameEditText;
    private EditText alertDaysEditText;
    private Button createButton;
    private Button cancelButton;


    private String nameString = "";
    private int alertDays = -1;

    public AlertTypeCreateDialogFragment() {
        // Required empty public constructor
    }

    public static AlertTypeCreateDialogFragment newInstance(String title, AlertTypeCreateListener listener){
        alertTypeCreateListener = listener;
        AlertTypeCreateDialogFragment alertTypeCreateDialogFragment = new AlertTypeCreateDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        alertTypeCreateDialogFragment.setArguments(args);

        alertTypeCreateDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return alertTypeCreateDialogFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_alert_type_create_dialog, container, false);
        nameEditText = view.findViewById(R.id.alertTypeNameEditText);
        alertDaysEditText = view.findViewById(R.id.typeDaysEditText);

        createButton = view.findViewById(R.id.createButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        String title = getArguments().getString(Constants.TITLE);
        getDialog().setTitle(title);
        final AppDatabase db = AppDatabase.getAppDatabase(getContext());

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameString = nameEditText.getText().toString();
                alertDays = Integer.parseInt(alertDaysEditText.getText().toString());

                AlertType alertType = new AlertType(nameString, alertDays);

                try {
                    long id = db.alertTypeDAO().insertAlertType(alertType);
                    if(id>0){
                        getDialog().dismiss();
                        alertType.setId(id);
                        alertTypeCreateListener.onAlertTypeCreated(alertType);
                        Toast.makeText(getContext(), "Student created successfully", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex){
                    getDialog().dismiss();
                    Toast.makeText(getContext(), "Student created failed", Toast.LENGTH_LONG).show();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        return view;
    }

    /*
    private static class InsertTask extends AsyncTask<Void, Void, Boolean> {

        private WeakReference<Context> c;
        private AlertType alertType;

        InsertTask(Context context, AlertType alertType) {
            c = new WeakReference<>(context);
            this.alertType = alertType;
        }

        // doInBackground methods runs on a worker thread
        @Override
        protected Boolean doInBackground(Void... objs) {
            AppDatabase db = AppDatabase.getAppDatabase(c.get());
            db.alertTypeDAO().insertAlertType(alertType);
            return true;
        }

        // onPostExecute runs on main thread
        @Override
        protected void onPostExecute(Boolean bool) {
            if (bool) {
                Toast.makeText(c.get(), "Alert Type added successfully!", Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
            }
        }
    }
    */


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            //noinspection ConstantConditions
            dialog.getWindow().setLayout(width, height);
        }
    }

}
