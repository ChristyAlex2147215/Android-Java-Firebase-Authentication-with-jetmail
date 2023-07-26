package com.example.userauthetication;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.userauthetication.MainActivity;
import com.example.userauthetication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText LoginEmail, LoginPassword;
    private Button LoginButton;
    private TextView SignupRedirectText,ForgotPassword;

    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        LoginEmail = findViewById(R.id.login_email);
        LoginPassword = findViewById(R.id.login_password);
        LoginButton = findViewById(R.id.Login_Button);
        SignupRedirectText = findViewById(R.id.Signup_Text);
        ForgotPassword=findViewById(R.id.login_forgotpass);

        LoginPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Check if the eye button (drawableRight) was pressed
                if (event.getAction() == MotionEvent.ACTION_UP && event.getRawX() >= LoginPassword.getRight() - LoginPassword.getCompoundDrawables()[2].getBounds().width()) {
                    togglePasswordVisibility();
                    return true;
                }
                return false;
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = LoginEmail.getText().toString().trim();
                String password = LoginPassword.getText().toString().trim();

                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (!password.isEmpty()) {
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                    }

                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        LoginPassword.setError("Password cannot be empty");
                    }
                } else {
                    LoginEmail.setError("Invalid email address");
                }
            }
        });
        SignupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }
        });
        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
            }
        });
    }

    private void togglePasswordVisibility() {
        LoginPassword=findViewById(R.id.login_password);
        if (isPasswordVisible) {
            // Hide the password
            LoginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            // Show the password
            LoginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }

        // Toggle the state
        isPasswordVisible = !isPasswordVisible;
    }
}

