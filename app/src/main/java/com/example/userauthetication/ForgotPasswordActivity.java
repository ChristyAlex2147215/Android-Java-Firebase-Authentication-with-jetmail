package com.example.userauthetication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ForgotPasswordActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText FpEmail;
    private Button FpButton;

    private TextView SignupRedirectText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        FpButton=findViewById(R.id.fp_Button);
        FpEmail=findViewById(R.id.fp_email);
        mAuth=FirebaseAuth.getInstance();
        SignupRedirectText=findViewById(R.id.Signup_Text);

        //for the submit button
        FpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=FpEmail.getText().toString().trim();
                if(email.isEmpty())
                {
                    FpEmail.setError("Email cannot be Empty");
                }
                else {
                    sendPasswordResetEmail(email);
                }

            }
        });
        SignupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPasswordActivity.this,SignUpActivity.class));
            }
        });

    }
    private void sendPasswordResetEmail(String emailAddress) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // Start the password reset process
        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Password reset email sent successfully
                            Toast.makeText(ForgotPasswordActivity.this, "Password reset email sent.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Failed to send password reset email
                            Toast.makeText(ForgotPasswordActivity.this, "Failed to send password reset email.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}