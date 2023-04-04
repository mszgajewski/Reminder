package com.mszgajewski.reminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mszgajewski.reminder.databinding.ActivityHomeBinding;
import com.mszgajewski.reminder.databinding.ActivityLoginBinding;

import java.text.DateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    private AlarmReceiver alarmReceiver;
    ActivityHomeBinding binding;
    private DatabaseReference reference;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String onlineUserID;
    DelayedProgressDialog loader = new DelayedProgressDialog();
    private String key = "";
    private String task;
    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.homeToolbar);
        getSupportActionBar().setTitle("Reminder");
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(linearLayoutManager);

/*
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        onlineUserID = mUser.getUid();
        loader = new ProgressDialog(this);


        if(mUser!= null)
        {

        } else {
            Intent intent = new Intent(HomeActivity.this, RegistrationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
*/
        reference = FirebaseDatabase.getInstance().getReference().child("Area").child("Majków");
    }

    private void setAlarm() {
    }

    @Override
    protected void onStart() {
        super.onStart();

        alarmReceiver = new AlarmReceiver();

        FirebaseRecyclerOptions<Model> options = new FirebaseRecyclerOptions.Builder<Model>()
                .setQuery(reference, Model.class).build();

        FirebaseRecyclerAdapter<Model, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Model, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Model model) {
                holder.setDate(model.getDate());
                holder.setTask(model.getTask());
                holder.setDesc(model.getDescription());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        key = getRef(position).getKey();
                        task = model.getTask();
                        description = model.getDescription();

                      // updateTask();
                    }
                });
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.retrieved_layout, parent, false);
                return new MyViewHolder(view);
            }

            @Override
            public void onError(@NonNull DatabaseError error) {
                super.onError(error);
            }

        };

        binding.recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            mAuth.signOut();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTask(String task) {
            TextView taskTextView = mView.findViewById(R.id.dateTv);
            taskTextView.setText(task);
        }

        public void setDesc(String desc) {
            TextView descTextView = mView.findViewById(R.id.descriptionTv);
            descTextView.setText(desc);
        }

        public void setDate(String date) {
            TextView dateTextView = mView.findViewById(R.id.dateTv);
            dateTextView.setText(date);
        }
    }
}