package com.mszgajewski.reminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText registrationEmail, registrationPassword;
    private Button registrationButton;
    private TextView registrationQuestion;
    private FirebaseAuth mAuth;
    private ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);

        toolbar = findViewById(R.id.registrationToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");

        mAuth = FirebaseAuth.getInstance();
        loader = new ProgressDialog(this);

        registrationEmail =findViewById(R.id.registrationEmail);
        registrationPassword =findViewById(R.id.registrationPassword);
        registrationButton =findViewById(R.id.registrationButton);
        registrationQuestion =findViewById(R.id.registrationPageQuestion);

        registrationQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = registrationEmail.getText().toString().trim();
                String password = registrationPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    registrationEmail.setError("Email jest wymagany");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    registrationEmail.setError("Hasło jest wymagane");
                    return;
                } else {
                    loader.setMessage("Rejestrowanie");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();
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