package com.mszgajewski.reminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.mszgajewski.reminder.databinding.ActivityRegistrationBinding;

public class RegistrationActivity extends AppCompatActivity {

    ActivityRegistrationBinding binding;

    private FirebaseAuth mAuth;
    private DelayedProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.registrationToolbar);
        getSupportActionBar().setTitle("Zarejestruj się");

        mAuth = FirebaseAuth.getInstance();
        loader = new DelayedProgressDialog();

        binding.registrationPageQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        binding.registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.registrationEmail.getText().toString().trim();
                String password = binding.registrationPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    binding.registrationEmail.setError("Email jest wymagany");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    binding.registrationEmail.setError("Hasło jest wymagane");
                    return;
                } else {
                    loader.show(getSupportFragmentManager(),"Rejestrowanie");
                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                                loader.dismiss();
                            } else {
                                String error = task.getException().toString();
                                Toast.makeText(RegistrationActivity.this,"Rejestracja nieudana" + error, Toast.LENGTH_SHORT).show();
                                loader.dismiss();
                            }
                        }
                    });
                }

            }
        });
    }
}