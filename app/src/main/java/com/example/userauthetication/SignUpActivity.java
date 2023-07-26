package com.example.userauthetication;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText SignupEmail, SignupPassword,SignupConfirmPassword,SignUpOTP;
    private Button SignupButton;
    private TextView loginRedirectText;
    private Button SentOtpButton;

    private Boolean OTPVerified=false;

    private Boolean isPassword1Visible = false;
    private  Boolean isPassword2Visible = false;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        mAuth = FirebaseAuth.getInstance();
        SignupEmail = findViewById(R.id.signup_email);
        SignupPassword = findViewById(R.id.signup_password);
        SignupButton = findViewById(R.id.Signup_Button);
        loginRedirectText = findViewById(R.id.Login_Text);
        SentOtpButton=findViewById(R.id.OTP_Button);
        SignupConfirmPassword=findViewById(R.id.signup_confirm_password);
        SignUpOTP=findViewById(R.id.Signup_Otp);



        SignupPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Check if the eye button (drawableRight) was pressed
                if (event.getAction() == MotionEvent.ACTION_UP && event.getRawX() >= SignupPassword.getRight() - SignupPassword.getCompoundDrawables()[2].getBounds().width()) {
                    togglePassword1Visibility();
                    return true;
                }
                return false;
            }
        });
        SignupConfirmPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Check if the eye button (drawableRight) was pressed
                if (event.getAction() == MotionEvent.ACTION_UP && event.getRawX() >= SignupConfirmPassword.getRight() - SignupConfirmPassword.getCompoundDrawables()[2].getBounds().width()) {
                    togglePassword2Visibility();
                    return true;
                }
                return false;
            }
        });


        SentOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = SignupEmail.getText().toString().trim();
                String password = SignupPassword.getText().toString().trim();
                String confirmPassword=SignupConfirmPassword.getText().toString().trim();
                if (email.isEmpty()) {
                    SignupEmail.setError("Email cannot be empty");
                    Log.w("TAG", "Email is Empty");
                }
                else if(!confirmPassword.equals(password))  {
                    SignupPassword.setError("Passwords are not matching");
                    SignupConfirmPassword.setError("Passwords are not matching");
                    Log.d("TAG","Passwords are not matching");
                }

                else {
                    sendOTP(email,mAuth);
                }
            }
        });

        SignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG","Signup Button is pressed");
                String email = SignupEmail.getText().toString().trim();
                String password = SignupPassword.getText().toString().trim();
                String ConfirmPassword=SignupConfirmPassword.getText().toString().trim();
                String OTP=SignUpOTP.getText().toString().trim();

                if (email.isEmpty() && !email.contains("@")) {
                    SignupEmail.setError("Email is not valid");
                } else if (password.isEmpty()) {
                    SignupPassword.setError("Password cannot be Empty");
                } else if (!password.equals(ConfirmPassword)) {
                    SignupConfirmPassword.setError("Passwords are not matching");

                }
                else if(OTP.isEmpty())
                {
                    SignUpOTP.setError("OTP cannot be Empty");
                }else {
                    OTPVerified=verifyOTP(OTP,email,mAuth);

                    if(OTPVerified)
                    {
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(SignUpActivity.this, "SignUp Success", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                            finish(); // Close the SignUpActivity after successful sign-up
                                        } else {
                                            Toast.makeText(SignUpActivity.this, "Authentication failed: " +
                                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            Log.e("TAG","Error occured while creating new user");
                                        }
                                    }
                                });

                    }

                }
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }

    private void sendOTP(String email,FirebaseAuth mAuth) {
        try {
            new SentMail.SendMailTask(email, SignUpActivity.this).execute();
            Log.d("TAG","final OTP Created is => "+String.valueOf(SentMail.ran));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean verifyOTP(String userOTP, final String email, FirebaseAuth mAuth) {
        Log.d("TAG", "Function called to verify the OTP");

       if(String.valueOf(SentMail.ran).equals(userOTP))
       {

           Log.i("TAG",userOTP);
           Log.i("TAG",String.valueOf(SentMail.ran));
           Log.i("TAG","OTP are matching");
           // Alternatively, if you are using Firebase Phone Auth, you can directly use the `userOTP` for verification:
           AuthCredential credential = EmailAuthProvider.getCredential(email, userOTP);
           Toast.makeText(SignUpActivity.this, "OTP Verified Successfully", Toast.LENGTH_SHORT).show();
           return true;

           // Sign in with the credential
//           mAuth.signInWithCredential(credential)
//                   .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                       @Override
//                       public void onComplete(@NonNull Task<AuthResult> task) {
//                           if (task.isSuccessful()) {
//                               // OTP verification successful, proceed with login or other actions
//                               Toast.makeText(SignUpActivity.this, "OTP Verified Successfully", Toast.LENGTH_SHORT).show();
//                           } else {
//                               // OTP verification failed
//                               Toast.makeText(SignUpActivity.this, "OTP Verification Failed", Toast.LENGTH_SHORT).show();
//                           }
//                       }
//                   });
       }
       else {
           Log.i("TAG",userOTP);
           Log.i("TAG",String.valueOf(SentMail.ran));
           Log.i("TAG","OTP are not matching");
           Toast.makeText(SignUpActivity.this, "OTP Verification Failed", Toast.LENGTH_SHORT).show();
           return false;

       }


    }

    private void togglePassword1Visibility() {
        SignupPassword=findViewById(R.id.signup_password);
        if (isPassword1Visible) {
            // Hide the password
            SignupPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            // Show the password
            SignupPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }

        // Toggle the state
        isPassword1Visible = !isPassword1Visible;
    }

    private void togglePassword2Visibility() {
        SignupConfirmPassword=findViewById(R.id.signup_confirm_password);
        if (isPassword2Visible) {
            // Hide the password
            SignupConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            // Show the password
            SignupConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }

        // Toggle the state
        isPassword2Visible = !isPassword2Visible;
    }



}
