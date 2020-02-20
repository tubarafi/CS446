package andriod.example.refresh.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import andriod.example.refresh.R;
import andriod.example.refresh.database.AppDatabase;
import andriod.example.refresh.database.model.AlertType;

public class TypeListAdapter extends RecyclerView.Adapter<TypeListAdapter.ListViewHolder> {

    private Context context;
    private List<AlertType> alertTypeList;
    private LayoutInflater layoutInflater;
    private AppDatabase db;

    public TypeListAdapter(Context context, List<AlertType> alertTypeList) {
        db = AppDatabase.getAppDatabase(context);
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.alertTypeList = alertTypeList;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.alert_type_item, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeListAdapter.ListViewHolder holder, int position) {
        final int itemPosition = position;
        final AlertType alertType = alertTypeList.get(position);

        holder.typeNameTextView.setText(alertType.getName());
        holder.typeDaysTextView.setText(String.valueOf(alertType.getNotificationDays()));

        /*
        holder.crossButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Are you sure, You wanted to delete this Alert Type?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                deleteStudent(itemPosition);
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
                StudentUpdateDialogFragment studentUpdateDialogFragment = StudentUpdateDialogFragment.newInstance(student.getRegistrationNumber(), itemPosition, new StudentUpdateListener() {
                    @Override
                    public void onStudentInfoUpdated(Student student, int position) {
                        studentList.set(position, student);
                        notifyDataSetChanged();
                    }
                });
                studentUpdateDialogFragment.show(((StudentListActivity) context).getSupportFragmentManager(), Config.UPDATE_STUDENT);
            }
        });

         */

    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView typeNameTextView;
        TextView typeDaysTextView;
        ImageView crossButtonImageView;
        ImageView editButtonImageView;

        public ListViewHolder(View itemView) {
            super(itemView);

            typeNameTextView = itemView.findViewById(R.id.typeNameTextView);
            typeDaysTextView = itemView.findViewById(R.id.typeDaysTextView);
            crossButtonImageView = itemView.findViewById(R.id.crossImageView);
            editButtonImageView = itemView.findViewById(R.id.editImageView);
        }
    }


    @Override
    public int getItemCount() {
        return alertTypeList.size();
    }
}
