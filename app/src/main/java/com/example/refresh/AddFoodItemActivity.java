package com.example.refresh;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputType;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.refresh.database.AppDatabase;
import com.example.refresh.database.model.FoodItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AddFoodItemActivity extends AppCompatActivity {

    private EditText nameEditText, quantityEditText, remindDateEditText, noteEditText;
    private Button createButton, cancelButton;
    private DatePickerDialog picker;

    private AppDatabase db;
    private FoodItem foodItem;
    private boolean dateSelected = false;
    private boolean update = false;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_item);
        nameEditText = findViewById(R.id.foodNameEditText);
        quantityEditText = findViewById(R.id.quantityEditText);
        remindDateEditText = findViewById(R.id.remindDateEditText);
        remindDateEditText.setInputType(InputType.TYPE_NULL);
        noteEditText = findViewById(R.id.noteEditText);
        createButton = findViewById(R.id.createButton);
        cancelButton = findViewById(R.id.cancelButton);
        db = AppDatabase.getAppDatabase(AddFoodItemActivity.this);
        pos = getIntent().getIntExtra("position", -1);

        if ((foodItem = (FoodItem) getIntent().getSerializableExtra("food_item")) != null) {
            getSupportActionBar().setTitle("Update Food Item");
            update = true;

            createButton.setText("Update");
            nameEditText.setText(foodItem.getName());
            quantityEditText.setText(String.valueOf(foodItem.getQuantity()));
            remindDateEditText.setText(foodItem.getRemindMeOnDate());
            noteEditText.setText(foodItem.getNote());
        } else {
            getSupportActionBar().setTitle("Create Food Item");
        }

        remindDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateSelected = true;
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                picker = new DatePickerDialog(AddFoodItemActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        remindDateEditText.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                    }
                }, year, month, day);
                picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                picker.show();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getApplicationContext();
                String itemName = nameEditText.getText().toString();
                String itemQuantity = quantityEditText.getText().toString();
                if (itemName.equals("")) {
                    Toast.makeText(context, "Please enter an item name.", Toast.LENGTH_LONG).show();
                } else if (itemQuantity.equals("")) {
                    Toast.makeText(context, "Please enter a quantity.", Toast.LENGTH_LONG).show();
                } else if (!dateSelected) {
                    Toast.makeText(context, "Please enter a date.", Toast.LENGTH_LONG).show();
                } else {
                    if (update) {
                        foodItem.setName(nameEditText.getText().toString());
                        foodItem.setQuantity(Integer.valueOf(quantityEditText.getText().toString()));
                        foodItem.setRemindMeOnDate(remindDateEditText.getText().toString());
                        foodItem.setNote(noteEditText.getText().toString());

                        try {
                            db.foodItemDAO().update(foodItem);
                            setResult(foodItem, 2); // update
                            Toast.makeText(context, "Updated " + foodItem.getName() + ".", Toast.LENGTH_LONG).show();

                        } catch (Exception ex) {
                            Log.e("Update Food failed", ex.getMessage());
                        }
                    } else {
                        foodItem = new FoodItem(nameEditText.getText().toString(), remindDateEditText.getText().toString(), Integer.valueOf(quantityEditText.getText().toString()), noteEditText.getText().toString());
                        try {
                            db.foodItemDAO().insert(foodItem);
                            setResult(foodItem, 1); //create
                            Toast.makeText(context, "Added " + foodItem.getName() + " to fridge.", Toast.LENGTH_LONG).show();

                            scheduleNotification(context, foodItem);
                        } catch (Exception ex) {
                            Log.e("Add Food failed", ex.getMessage());
                        }
                    }
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setResult(FoodItem food, int flag) {
        setResult(flag, new Intent().putExtra("food_item", food).putExtra("position", pos));
        finish();
    }

    private void scheduleNotification(Context context, FoodItem foodItem) {
        // Create notification channel
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("pls_work", "ReFresh", importance);
        // Create notification content
        NotificationCompat.Builder builder = new NotificationCompat.Builder(AddFoodItemActivity.this, "pls_work")
                .setContentTitle("Scheduled Notification")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(foodItem.getName() + " is expiring today! Browse recipes that use it now."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_app_icon)
                .setAutoCancel(true);
        // Build notification with nav intent
        Intent notifyIntent = new Intent(AddFoodItemActivity.this, MainActivity.class);
        notifyIntent.putExtra("menuFragment", "RecipeFragment");
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(AddFoodItemActivity.this, 0, notifyIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(contentIntent);
        Notification notification = builder.build();
        // Create intent to be scheduled
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, foodItem.getId());
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, 0);
        // Schedule
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("EST"));

        try {
            Date expiryDate = new SimpleDateFormat("MM/dd/yyyy").parse(foodItem.getRemindMeOnDate());
            if (DateUtils.isToday(expiryDate.getTime())) {
                alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), alarmIntent);
            } else {
                calendar.setTime(expiryDate);
                calendar.set(Calendar.HOUR_OF_DAY, 12);
                calendar.set(Calendar.MINUTE, 40);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
            }
        } catch (Exception ex) {
            Log.e("Date conversion for notification failed.", ex.getMessage());
        }
    }
}
