package com.example.refresh;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Calendar;
import java.util.Objects;

public class NotificationSettingsActivity extends AppCompatActivity {
    private Button cancelButton, addReminderButton;
    private EditText remindTimeEditText;
    private TimePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Notification Settings");

        remindTimeEditText = findViewById(R.id.remindTimeEditText);
        addReminderButton = findViewById(R.id.add_button);
        cancelButton = findViewById(R.id.cancelButton);

        remindTimeEditText.setOnClickListener(view -> {
            Calendar currentTime = Calendar.getInstance();
            int hour = currentTime.get(Calendar.HOUR_OF_DAY);
            int minute = currentTime.get(Calendar.MINUTE);

            picker = new TimePickerDialog(NotificationSettingsActivity.this, (timePicker, selectedHour, selectedMinute) -> remindTimeEditText.setText((selectedHour > 12 ? selectedHour - 12 : selectedHour) + ":" + selectedMinute + (selectedHour > 12 ? " PM" : " AM")), hour, minute, false);
            picker.show();
        });

        addReminderButton.setOnClickListener(this::openDialog);
        cancelButton.setOnClickListener(view -> finish());
    }

    public void openDialog(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_notification);
        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);

        Button dialogCreateButton = dialog.findViewById(R.id.createButton);
        Button dialogCancelButton = dialog.findViewById(R.id.cancelButton);

        dialogCreateButton.setOnClickListener(v -> dialog.dismiss());
        dialogCancelButton.setOnClickListener(v -> dialog.dismiss());
    }
}
