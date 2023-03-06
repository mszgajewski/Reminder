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

import com.mszgajewski.reminder.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    DelayedProgressDialog loader = new DelayedProgressDialog();

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.loginToolbar);
        getSupportActionBar().setTitle("Zaloguj się");

        mAuth = FirebaseAuth.getInstance();

        loader = new DelayedProgressDialog();

        if (mAuth != null){
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        }

        binding.loginPageQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.loginEmail.getText().toString().trim();
                String password = binding.loginPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    binding.loginEmail.setError("Email jest wymagany");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    binding.loginEmail.setError("Hasło jest wymagane");
                    return;
                } else {
                    loader.show(getSupportFragmentManager(), "Logowanie");

                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                String error = task.getException().toString();
                                Toast.makeText(LoginActivity.this, "Logowanie nieudane" + error, Toast.LENGTH_SHORT).show();
                            }
                            loader.cancel();
                        }
                    });
                }
            }
        });
    }
}